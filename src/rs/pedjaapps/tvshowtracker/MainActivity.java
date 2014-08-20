package rs.pedjaapps.tvshowtracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PoppyViewHelper;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rs.pedjaapps.tvshowtracker.adapter.ShowsAdapter;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Comparators;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
    private static final int REQUEST_CODE_LOGIN = 9001;
    ShowsAdapter adapter;
    GridView list;
    ProgressBar pbLoading;
    TextView tvNoShows;
    SearchView searchView;
    private SortOrder sort;
    Menu menu;
    boolean menuDisable;

    SlidingMenu sideMenu;
    LinearLayout llUpcomingEpisode;
    Episode upcomingEpisode;
    TextView tvUpcomingEpisode;
    TextView tvLoginLogout;

    PoppyViewHelper poppyViewHelper;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        MainApp.getInstance().getDaoSession();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sideMenu = new SlidingMenu(this);
        sideMenu.setMode(SlidingMenu.LEFT);
        sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sideMenu.setShadowWidthRes(R.dimen.shadow_width);
        sideMenu.setShadowDrawable(R.drawable.menu_shadow);
        sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sideMenu.setFadeDegree(0.35f);
        sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sideMenu.setMenu(R.layout.menu_layout);

        findViewById(R.id.tvMenuCalendar).setOnClickListener(this);
        tvLoginLogout = (TextView) findViewById(R.id.tvMenuLoginLogout);
        tvLoginLogout.setOnClickListener(this);
        findViewById(R.id.tvMenuMissed).setOnClickListener(this);
        findViewById(R.id.tvMenuSettings).setOnClickListener(this);
        findViewById(R.id.tvMenuTrending).setOnClickListener(this);

        setupUser();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getActionBar().getThemedContext());
        searchView.setQueryHint(getString(R.string.add_show));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryRefinementEnabled(true);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        tvNoShows = (TextView) findViewById(R.id.tvNoShows);
        setNoImageText();

        poppyViewHelper = new PoppyViewHelper(this, PoppyViewHelper.PoppyViewPosition.BOTTOM);
        View poppyView = poppyViewHelper.createPoppyViewOnGridView(R.id.lvShows, R.layout.upcoming_episode_layout);

        adapter = new ShowsAdapter(this);
        list = (GridView) findViewById(R.id.lvShows);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                startActivity(new Intent(MainActivity.this,
						ShowDetailsActivity.class).putExtra(ShowDetailsActivity.EXTRA_TVDB_ID,
						adapter.getItem(arg2).getTvdb_id()));
            }
        });
        llUpcomingEpisode = (LinearLayout)poppyView.findViewById(R.id.llUpcomingEpisode);
        tvUpcomingEpisode = (TextView)poppyView.findViewById(R.id.tvUpcomingEpisode);

        Utility.setRefresh(true);
    }

    private void setupUser()
    {
        String user = MainApp.getInstance().getActiveUser();
        if(!PrefsManager.defaultUser.equals(user))
        {
            getActionBar().setSubtitle(user);
            tvLoginLogout.setText(R.string.logout);
        }
        else
        {
            tvLoginLogout.setText(R.string.login);
        }
    }

    private void setNoImageText()
    {
        String text = getString(R.string.no_shows_message);
        SpannableStringBuilder builder = new SpannableStringBuilder(Html.fromHtml(text));
        builder.setSpan(new ImageSpan(this, R.drawable.ic_action_search_dark), 22, 23, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvNoShows.setText(builder, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void onResume()
    {
        if (Utility.isRefresh())
        {
            refreshShows();
        }
        super.onResume();
    }

    public void refreshShows()
    {
        //profile = prefs.getString("profile", "Default");
        sort = PrefsManager.getSortOrder();
        new LoadShows().execute();
        Utility.setRefresh(false);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Restore the previously serialized current dropdown position.
        list.onRestoreInstanceState(savedInstanceState.getParcelable("list_position"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // Serialize the current dropdown position.
        Parcelable state = list.onSaveInstanceState();
        outState.putParcelable("list_position", state);
    }

    private List<Show> getShows()
    {
        long startTime = System.currentTimeMillis();
        ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
        List<Show> shows = showDao.loadAll();

		upcomingEpisode = Utility.calculateUpcomingEpisodes(shows);

        //TODO query db with sort, it should be faster that sorting list
		switch (sort)
        {
            case show_name:
                Collections.sort(shows, new Comparators.NameComparator(false));
                break;
            case next_episode:
                Collections.sort(shows, new Comparators.NextEpisodeComparator(true));
                break;
            case unwatched_episodes:
                Collections.sort(shows, new Comparators.WatchComparator(true));
                break;
            case network:
                Collections.sort(shows, new Comparators.NetworkComparator(true));
                break;
            case rating:
                Collections.sort(shows, new Comparators.RatingComparator(false));
                break;
            case runtime:
                Collections.sort(shows, new Comparators.RuntimeComparator(true));
                break;
            case status:
                Collections.sort(shows, new Comparators.StatusComparator(true));
                break;
        }
		Log.d(Constants.LOG_TAG,
				"MainActivity.java > getShows(): "
						+ (System.currentTimeMillis() - startTime) + "ms");

        return shows;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvMenuCalendar:
                break;
            case R.id.tvMenuLoginLogout:
                if(PrefsManager.defaultUser.equals(MainApp.getInstance().getActiveUser()))
                {
                    startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
                }
                else
                {
                    //new ATLogout();
                }
                break;
            case R.id.tvMenuMissed:
                break;
            case R.id.tvMenuSettings:
                break;
            case R.id.tvMenuTrending:
               break;
        }
    }


    public class LoadShows extends AsyncTask<String, Void, List<Show>>
    {

        @Override
        protected List<Show> doInBackground(String... args)
        {
            long startTime = System.currentTimeMillis();
            List<Show> entry = new ArrayList<Show>();
            for (Show s : getShows())
            {
                entry.add(s);
            }
            Log.d(Constants.LOG_TAG,
                    "MainActivity.java > LoadShows > doInBackground: "
                            + (System.currentTimeMillis() - startTime) + "ms"
            );
            return entry;
        }

        @Override
        protected void onPreExecute()
        {
            if(adapter.isEmpty())
            {
                pbLoading.setVisibility(View.VISIBLE);
                //tvNoShows.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(List<Show> result)
        {
            adapter.clear();
            for (Show s : result)
            {
                adapter.add(s);
            }
            adapter.notifyDataSetChanged();
            if(adapter.isEmpty())
            {
                tvNoShows.setVisibility(View.VISIBLE);
            }
            else
            {
                tvNoShows.setVisibility(View.GONE);
            }
            pbLoading.setVisibility(View.GONE);
            if(upcomingEpisode != null)
            {
                tvUpcomingEpisode.setText(Utility.generateUpcomingEpisodeText(upcomingEpisode));
                llUpcomingEpisode.setVisibility(View.VISIBLE);
            }
            else
            {
                llUpcomingEpisode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        System.out.println("onCreateOptionsMenu");
        //this.menu = menu;
        menu.add(0, 0, 0, getString(R.string.add))
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );
		/*menu.add(0, 1, 1, "Agenda").setIcon(R.drawable.ic_action_agenda)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		/*menu.add(0, 2, 2, "Preferences").setIcon(R.drawable.ic_action_settings)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        menu.add(0, 3, 3, getString(R.string.update_all)).setIcon(R.drawable.ic_action_update)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
	
	/*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerContent);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		/*if(mDrawerLayout.isDrawerOpen(drawerContent))
		{
		    mDrawerLayout.closeDrawer(drawerContent);
		}
		/*if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
        }*/
        switch (item.getItemId())
        {
            case 1:
                //startActivity(new Intent(this, AgendaActivity.class).putExtra("profile", profile));
                return true;
            case 2:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case 3:
                /*if (Tools.isNetworkAvailable(this))
                {
                    new UpdateShow().execute(db.getAllShows(prefs.getString("filter", "all"), profile, prefs.getString("sort", "id"), ""));
                }
                else
                {
                    Toast.makeText(
                            MainActivity.this,
                            "No Internet Connection!\nPlease connect to internet and try again!",
                            Toast.LENGTH_LONG).show();
                }*/
                //TODO update all shows
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public class ListActionMode implements ActionMode.Callback
    {
        int id;
        int position;

        public ListActionMode(int position)
        {
            this.position = position;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            //MainActivity.this.mode = mode;

			// mode.setTitle("Select Items");
			// mode.setSubtitle("One item selected");

            menu.add(0, 0, 0, getString(R.string.delete))
                    .setIcon(R.drawable.ic_action_delete)
                    .setShowAsAction(
                            MenuItem.SHOW_AS_ACTION_IF_ROOM
                                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT
                    );
            menu.add(0, 1, 1, getString(R.string.update))
                    .setIcon(R.drawable.ic_action_update)
                    .setShowAsAction(
                            MenuItem.SHOW_AS_ACTION_IF_ROOM
                                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT
                    );

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {

            switch (item.getItemId())
            {
                case 0:
				//db.deleteSeries(adapter.getItem(position).getSeriesId() + "",profile);
				//adapter.remove(adapter.getItem(position));
				//adapter.notifyDataSetChanged();
				//setUI(Constants.UI_CODE_AFTERLOAD);
				//mode.finish();
                    break;
                case 1:
                    //if (Tools.isNetworkAvailable(MainActivity.this))
                    //{
                    //    List<ShowOld> list = new ArrayList<ShowOld>();
                        //list.add(db.getShow(adapter.getItem(position).getSeriesId()
                        //	+ "", profile));
                    //    new UpdateShow().execute(list);
                    //}
                    //else
                    //{
                    //    Toast.makeText(
                    //            MainActivity.this,
                    //            "No Internet Connection!\nPlease connect to internet and try again!",
                    //            Toast.LENGTH_LONG).show();
                    //}
                    //TODo update show
                    //mode.finish();
                    break;
            }

            return true;
        }

    }*/

    /*public class UpdateShow extends AsyncTask<List<ShowOld>, String[], String>
    {

        ProgressDialog pd;

        @Override
        protected String doInBackground(List<ShowOld>... args)
        {
            List<ShowOld> shows = new ArrayList<ShowOld>();
            List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
            List<ActorOld> actors = new ArrayList<ActorOld>();
            for (int n = 0; n < args[0].size(); n++)
            {
                publishProgress(new String[]{n + 1 + "",
                        args[0].get(n).getSeriesName(), args[0].size() + ""});

                ShowOld s = args[0].get(n);
                if (!new File(extStorage + "/TVST/actors").exists())
                {
                    new File(extStorage + "/TVST/actors").mkdirs();
                }
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl("http://thetvdb.com/api/"
                        + Constants.apiKey + "/series/" + s.getSeriesId()
                        + "/all/" + s.getLanguage() + ".xml");

                Document doc = parser.getDomElement(xml); // getting DOM element

                NodeList nl = doc.getElementsByTagName("Series");
                Element e = (Element) nl.item(0);
                String date = Constants.df.format(new Date());
                String seriesId = parser.getValue(e, "id");
                String banner = "";
                String fanart = "";
                try
                {
                    banner = Tools.DownloadFromUrl(
                            "http://thetvdb.com/banners/"
                                    + parser.getValue(e, "banner"),
                            extStorage
                                    + "/TVST"
                                    + parser.getValue(e, "banner")
                                    .substring(
                                            parser.getValue(
                                                    e,
                                                    "banner")
                                                    .lastIndexOf(
                                                            "/")
                                    ), true
                    );
                    fanart = Tools.DownloadFromUrl(
                            "http://thetvdb.com/banners/"
                                    + parser.getValue(e, "fanart"),
                            extStorage
                                    + "/TVST"
                                    + parser.getValue(e, "fanart")
                                    .substring(
                                            parser.getValue(
                                                    e,
                                                    "fanart")
                                                    .lastIndexOf(
                                                            "/")
                                    ), true
                    );
                }
                catch (Exception exc)
                {

                }

                shows.add(
                        new ShowOld(
                                parser.getValue(e, "SeriesName"),
                                parser.getValue(e, "FirstAired"),
                                parser.getValue(e, "IMDB_ID"),
                                parser.getValue(e, "Overview"),
                                Tools.parseRating(parser.getValue(e, "Rating")),
                                Integer.parseInt(parser.getValue(e, "id")),
                                parser.getValue(e, "Language"),
                                banner,
                                fanart,
                                parser.getValue(e, "Network"),
                                Tools.parseInt(parser.getValue(e, "Runtime")),
                                parser.getValue(e, "Status"), false, false,
                                date, parser.getValue(e, "Actors"), profile)
                );

                nl = doc.getElementsByTagName("Episode");

                for (int i = 0; i < nl.getLength(); i++)
                {
                    e = (Element) nl.item(i);
                    if (!parser.getValue(e, "SeasonNumber").equals("0"))
                    {

                        episodes.add(
                                new EpisodeItem(parser.getValue(e,
                                        "EpisodeName"), Tools
                                        .parseInt(parser.getValue(e,
                                                "EpisodeNumber")), Tools
                                        .parseInt(parser.getValue(e,
                                                "SeasonNumber")), parser
                                        .getValue(e, "FirstAired"), parser
                                        .getValue(e, "IMDB_ID"), parser
                                        .getValue(e, "Overview"), Tools
                                        .parseRating(parser.getValue(e,
                                                "Rating")), false,
                                        Tools.parseInt(parser.getValue(e,
                                                "id")), profile, seriesId
                                )
                        );

                    }
                }
                xml = parser.getXmlFromUrl("http://thetvdb.com/api/"
                        + Constants.apiKey + "/series/" + s.getSeriesId()
                        + "/actors.xml");
                doc = parser.getDomElement(xml);
                nl = doc.getElementsByTagName("Actor");
                for (int i = 0; i < nl.getLength(); i++)
                {
                    e = (Element) nl.item(i);
                    String image = "";
                    try
                    {
                        String url = parser.getValue(e, "Image");
                        image = Tools.DownloadFromUrl(
                                "http://thetvdb.com/banners/"
                                        + url,
                                extStorage
                                        + "/TVST/actors"
                                        + url.substring(url.lastIndexOf("/")), true
                        );
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }


                    actors.add(
                            new ActorOld(
                                    parser.getValue(e, "id"),
                                    parser.getValue(e, "Name"),
                                    parser.getValue(e, "Role"),
                                    image,
                                    profile, seriesId)
                    );

                }
            }
            //db.insertActors(actors);
            //db.insertEpisodes(episodes);
            //db.insertShows(shows);
            return "";

        }

        @Override
        protected void onProgressUpdate(String[]... progress)
        {

            pd.setMessage("Updating " + (progress[0])[1] + " - "
                    + (progress[0])[0] + "/" + (progress[0])[2]);

        }

        @Override
        protected void onPreExecute()
        {
            Tools.setKeepScreenOn(MainActivity.this, true);
            pd = new ProgressDialog(MainActivity.this);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setTitle("Downloading Show Info");
            pd.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            pd.dismiss();
            new LoadShows().execute();
            Tools.setKeepScreenOn(MainActivity.this, false);
        }
    }*/

    @Override
    public void onDestroy()
    {
        Utility.setKeepScreenOn(MainActivity.this, false);
        //db.close();
        super.onDestroy();
    }

	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (menu != null)
		{
			for (int i = 0; i < menu.size(); i++)
			{
				if (menuDisable)
					menu.getItem(i).setEnabled(false);
				else
					menu.getItem(i).setEnabled(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}*/

    public enum SortOrder
    {
        id, show_name, next_episode, unwatched_episodes, network ,rating, runtime, status
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_LOGIN:
                    setupUser();
                    break;
            }
        }
    }
}
