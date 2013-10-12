package rs.pedjaapps.trakttvandroid;

import android.widget.*;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import rs.pedjaapps.trakttvandroid.MainActivity;
import rs.pedjaapps.trakttvandroid.adapter.DrawerAdapter;
import rs.pedjaapps.trakttvandroid.adapter.ShowsAdapter;
import rs.pedjaapps.trakttvandroid.model.Actor;
import rs.pedjaapps.trakttvandroid.model.DrawerItem;
import rs.pedjaapps.trakttvandroid.model.EpisodeItem;
import rs.pedjaapps.trakttvandroid.model.Show;
import rs.pedjaapps.trakttvandroid.utils.Constants;
import rs.pedjaapps.trakttvandroid.utils.Tools;
import rs.pedjaapps.trakttvandroid.utils.XMLParser;

public class MainActivity extends BaseActivity
{

	ShowsAdapter adapter;
	GridView list;
	ProgressBar loading;
	TextView listEmpty;
	SearchView searchView;
	ActionMode mode;
	String extStorage = Environment.getExternalStorageDirectory().toString();
	SharedPreferences prefs;
	String profile;
	String sort;
	Menu menu;
	boolean menuDisable;
	ArrayAdapter<String> profilesAdapter;
	Spinner profiles;
	Spinner sortSpinner;
	Spinner filter;
	
	boolean profileFirstSelect = true;
	boolean sortFirstSelect = true;
	boolean filterFirstSelect = true;
	
	SlidingMenu sideMenu;
	
	private RelativeLayout drawerContent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sideMenu = new SlidingMenu(this);
        sideMenu.setMode(SlidingMenu.LEFT);
        sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sideMenu.setShadowWidthRes(R.dimen.shadow_width);
        sideMenu.setShadowDrawable(R.drawable.menu_shadow);
        sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sideMenu.setFadeDegree(0.35f);
        sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sideMenu.setMenu(R.layout.menu_layout);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = new SearchView(getActionBar().getThemedContext());
		searchView.setQueryHint("Add new Movie");
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setQueryRefinementEnabled(true);

		loading = (ProgressBar) findViewById(R.id.pgrSearch);
		listEmpty = (TextView) findViewById(R.id.txtMessage);
		adapter = new ShowsAdapter(this, R.layout.shows_list_row);
		list = (GridView) findViewById(R.id.list);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				startActivity(new Intent(MainActivity.this,
						DetailsActivity.class).putExtra("seriesId",
						adapter.getItem(arg2).getSeriesId()).putExtra(
						"profile", profile));

			}

		});

		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				if (mode != null)
				{
					mode.finish();
				}
				mode = startActionMode(new ListActionMode(position));
				return true;
			}
		});
		//mTitle = mDrawerTitle = getTitle();
		drawerContent = (RelativeLayout)findViewById(R.id.left_drawer);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		ListView drawerList = (ListView)findViewById(R.id.drawer_list);
		DrawerAdapter dAdapter = new DrawerAdapter(this, R.layout.drawer_menu_item);
		dAdapter.add(new DrawerItem("Calendar", R.drawable.ic_action_agenda));
        dAdapter.add(new DrawerItem("Backlog", R.drawable.ic_action_backlog));
        dAdapter.add(new DrawerItem("Preferences", R.drawable.ic_action_settings));
		dAdapter.add(new DrawerItem("About", R.drawable.ic_action_about));
		drawerList.setAdapter(dAdapter);
		Tools.setListViewHeightBasedOnChildren(drawerList);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:
                        startActivity(new Intent(MainActivity.this, AgendaActivity.class).putExtra(
                                "profile", profile));
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, AgendaActivity.class).putExtra(
                                //"profile", profile));
                        Toast.makeText(MainActivity.this, "TODO: backlog", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, About.class));
                       // Toast.makeText(MainActivity.this, "TODO: about", Toast.LENGTH_LONG).show();
                        break;
                }
				closeDrawer();
            }
        });

        ((LinearLayout)findViewById(R.id.poweredBy)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://thetvdb.com")));
            }
        });

        profiles = (Spinner)findViewById(R.id.profile);
        profilesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        profiles.setAdapter(profilesAdapter);
		profiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> p1, View p2, int pos, long p4)
				{
					if(profileFirstSelect)
					{
						profileFirstSelect = false;
					}
					else
					{
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("profile", profilesAdapter.getItem(pos));
					editor.apply();
					closeDrawer();
					refreshShows();
					}
				}

				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			}
		);
        
        sortSpinner = (Spinner)findViewById(R.id.sort);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.pref_sort_titles));
        sortSpinner.setAdapter(sortAdapter);
		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> p1, View p2, int pos, long p4)
				{
					if(sortFirstSelect)
					{
						sortFirstSelect = false;
					}
					else
					{
					List<String> sortOptions = Arrays.asList(getResources().getStringArray(R.array.pref_sort_values));
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("sort", sortOptions.get(pos));
					editor.apply();
					closeDrawer();
					refreshShows();
					}
				}

				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			}
		);

        filter = (Spinner)findViewById(R.id.filter);
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.pref_filter_titles));
        filter.setAdapter(filterAdapter);
		filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> p1, View p2, int pos, long p4)
				{
					if(filterFirstSelect)
					{
						filterFirstSelect = false;
					}
					else
					{
					List<String> filterOptions = Arrays.asList(getResources().getStringArray(R.array.pref_filter_values));
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("filter", filterOptions.get(pos));
					editor.apply();
					closeDrawer();
					refreshShows();
					}
				}

				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			}
		);
		
		Tools.setRefresh(true);
	}

	private void refreshDrawer()
	{
		profilesAdapter.clear();
		profilesAdapter.addAll(db.getAllProfiles());
        profiles.setSelection(profilesAdapter.getPosition(prefs.getString("profile", "Default")));
		
		List<String> sortOptions = Arrays.asList(getResources().getStringArray(R.array.pref_sort_values));
		List<String> filterOptions = Arrays.asList(getResources().getStringArray(R.array.pref_filter_values));
		
		sortSpinner.setSelection(sortOptions.indexOf(prefs.getString("sort", "default")));
		filter.setSelection(filterOptions.indexOf(prefs.getString("filter", "all")));
	}
	
	private void closeDrawer()
	{
		sideMenu.toggle(true);
	}
	
	@Override
	protected void onResume()
	{
	    if (Tools.isRefresh())
		{
		refreshShows();
		}
		super.onResume();
	}
	
	private void refreshShows()
	{
	
			profile = prefs.getString("profile", "Default");
			sort = prefs.getString("sort", "default");
			StringBuilder b = new StringBuilder();
			b.append(profile);
			if (sort.equals("name"))
			{
				b.append(" | Sorted by Name");
			}
			else if (sort.equals("next"))
			{
				b.append(" | Sorted by Next Episode");
			}
			else if (sort.equals("unwatched"))
			{
				b.append(" | Sorted by Unwatched Episodes");
			}
			getActionBar().setSubtitle(b.toString());
			new LoadShows().execute();
			refreshDrawer();
			Tools.setRefresh(false);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		// Restore the previously serialized current dropdown position.
		list.onRestoreInstanceState(savedInstanceState
				.getParcelable("list_position"));

	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current dropdown position.
		Parcelable state = list.onSaveInstanceState();
		outState.putParcelable("list_position", state);
	}

	@SuppressLint("NewApi")
	private void setUI(int code)
	{

		if (code == Constants.UI_CODE_PRELOAD)
		{
			//menuDisable = true;
			/*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
			{
				this.invalidateOptionsMenu();
			}*/
			//list.setEnabled(false);
			listEmpty.setVisibility(View.GONE);
			//loading.setVisibility(View.VISIBLE);
		}
		else if (code == Constants.UI_CODE_AFTERLOAD)
		{
			//menuDisable = false;
			/*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
			{
				this.invalidateOptionsMenu();
			}*/
			//list.setEnabled(true);
			if (adapter.isEmpty())
			{
				listEmpty.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
			}
			else
			{
				listEmpty.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
			}
		}

	}

	private List<Show> getShows()
	{
		long startTime = System.currentTimeMillis();
		List<Show> shows = new ArrayList<Show>();
		List<Show> dbShows = db.getAllShows(prefs.getString("filter", "all"),
				profile, prefs.getString("sort", "id"), "");

		for (Show s : dbShows)
		{
			List<EpisodeItem> episodeItems = db.getAllEpisodes(s.getSeriesId()+"",
					profile);
			int episodeCount = db.getEpisodesCount(s.getSeriesId()+"", profile);
			String[] ue = upcomingEpisode(episodeItems, s.getStatus());
			shows.add(new Show(s.getSeriesName(), s.getBanner(), ue[0],
					watchedPercent(episodeItems, episodeCount), s.getSeriesId(),
					Integer.parseInt(ue[1])));
		}
		
		/*if (sort.equals("name"))
		{
			Collections.sort(shows, new SortByName());
		}
		else*/ if (sort.equals("next"))
		{
			Collections.sort(shows, new SortByNextEpisode());
		}
		else if (sort.equals("unwatched"))
		{
			Collections.sort(shows, new SortByUnwatched());
		}
		Log.d(Constants.LOG_TAG,
				"MainActivity.java > getShows(): "
						+ (System.currentTimeMillis() - startTime) + "ms");
		
		return shows;
	}

	static class SortByName implements Comparator<Show>
	{
		@Override
		public int compare(Show s1, Show s2)
		{
			String sub1 = s1.getSeriesName();
			String sub2 = s2.getSeriesName();
			return sub1.compareTo(sub2);
		}

	}

	static class SortByNextEpisode implements Comparator<Show>
	{
		@Override
		public int compare(Show p1, Show p2)
		{
			if (p1.getNextEpisodeDays() > p2.getNextEpisodeDays())
				return 1;
			if (p1.getNextEpisodeDays() < p2.getNextEpisodeDays())
				return -1;

			return 0;
		}

	}

	static class SortByUnwatched implements Comparator<Show>
	{
		@Override
		public int compare(Show p1, Show p2)
		{
			if (p1.getPrgWatched() < p2.getPrgWatched())
				return -1;
			if (p1.getPrgWatched() > p2.getPrgWatched())
				return 1;
			return 0;
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
							+ (System.currentTimeMillis() - startTime) + "ms");
			return entry;
		}

		@Override
		protected void onPreExecute()
		{
			setUI(Constants.UI_CODE_PRELOAD);
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
			setUI(Constants.UI_CODE_AFTERLOAD);
		}
	}

	private String[] upcomingEpisode(List<EpisodeItem> episodeItems, String status)
	{
		long startTime = System.currentTimeMillis();
		StringBuilder b = new StringBuilder();
		long days = 999999999;
		// ^workaround for sorting.
		// This way series which doesn't have next episode(or can't be
		// calculated) will always go last
		if (status != null && status.equals("Ended"))
		{
			b.append("THIS SHOW HAS ENDED");
		}
		else
		{
			
			for (EpisodeItem e : episodeItems)
			{
				// date is in format "yyyy-MM-dd"
				try
				{
					Date firstAired = Constants.df.parse(e.getFirstAired());
					if (new Date().before(firstAired)
							|| (new Date().getTime() / (1000 * 60 * 60 * 24)) == (firstAired
									.getTime() / (1000 * 60 * 60 * 24)))
					{
						b.append("S");
						if (e.getSeason() <= 10)
						{
							b.append("0").append(e.getSeason());
						}
						else
						{
							b.append(e.getSeason());
						}
						b.append("E");
						if (e.getEpisode() <= 10)
						{
							b.append("0").append(e.getEpisode()).append(" ")
									.append(e.getEpisodeName())
									.append(" - AIRS ");
						}
						else
						{
							b.append(e.getEpisode()).append(" ")
									.append(e.getEpisodeName())
									.append(" - AIRS ");
						}
						days = ((firstAired.getTime() - System
								.currentTimeMillis()) / (1000 * 60 * 60 * 24));
						if (days > 0)
							b.append("in ").append(days).append(" DAYS");
						else
							b.append("TODAY");
						break;
					}
				}
				catch (Exception ex)
				{
				}
			}

			if (b.length() < 1)
			{
				b.append("NO INFORMATION ABOUT NEXT EPISODE");
			}
		}
		Log.d(Constants.LOG_TAG,
				"MainActivity.java > upcomingEpisode(): "
						+ (System.currentTimeMillis() - startTime) + "ms");
		
		return new String[] { b.toString(), days + "" };
	}

	private int watchedPercent(List<EpisodeItem> episodeItems, int episodeCount)
	{
		int watched = 0;
		for (EpisodeItem e : episodeItems)
		{
			if (e.isWatched())
			{
				watched++;
			}
		}
		return (int) ((double) watched / (double) episodeCount * 100.0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		System.out.println("onCreateOptionsMenu");
		//this.menu = menu;
		menu.add(0, 0, 0, "Add")
				.setIcon(R.drawable.ic_action_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		/*menu.add(0, 1, 1, "Agenda").setIcon(R.drawable.ic_action_agenda)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		/*menu.add(0, 2, 2, "Preferences").setIcon(R.drawable.ic_action_settings)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		menu.add(0, 3, 3, "Update All").setIcon(R.drawable.ic_action_update)
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
			startActivity(new Intent(this, AgendaActivity.class).putExtra(
					"profile", profile));
			return true;
		case 2:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case 3:
			if (Tools.isNetworkAvailable(this))
			{
				new UpdateShow().execute(db.getAllShows(prefs.getString("filter", "all"), profile, prefs.getString("sort", "id"), ""));
			}
			else
			{
				Toast.makeText(
						MainActivity.this,
						"No Internet Connection!\nPlease connect to internet and try again!",
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ListActionMode implements ActionMode.Callback
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
			MainActivity.this.mode = mode;
			/*
			 * mode.setTitle("Select Items");
			 * mode.setSubtitle("One item selected");
			 */
			menu.add(0, 0, 0, "Delete")
					.setIcon(R.drawable.ic_action_delete)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add(0, 1, 1, "Update")
					.setIcon(R.drawable.ic_action_update)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

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
				db.deleteSeries(adapter.getItem(position).getSeriesId() + "",
						profile);
				adapter.remove(adapter.getItem(position));
				adapter.notifyDataSetChanged();
				setUI(Constants.UI_CODE_AFTERLOAD);
				mode.finish();
				break;
			case 1:
				if (Tools.isNetworkAvailable(MainActivity.this))
				{
					List<Show> list = new ArrayList<Show>();
					list.add(db.getShow(adapter.getItem(position).getSeriesId()
							+ "", profile));
					new UpdateShow().execute(list);
				}
				else
				{
					Toast.makeText(
							MainActivity.this,
							"No Internet Connection!\nPlease connect to internet and try again!",
							Toast.LENGTH_LONG).show();
				}
				mode.finish();
				break;
			}

			return true;
		}

	}

	public class UpdateShow extends AsyncTask<List<Show>, String[], String>
	{

		ProgressDialog pd;

		@Override
		protected String doInBackground(List<Show>... args)
		{
            List<Show> shows = new ArrayList<Show>();
            List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
            List<Actor> actors = new ArrayList<Actor>();
            for (int n = 0; n < args[0].size(); n++)
			{
				publishProgress(new String[] { n + 1 + "",
						args[0].get(n).getSeriesName(), args[0].size() + "" });

				Show s = args[0].get(n);
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
				String banner ="";
				String fanart = "";
				try {
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
																	"/")), true);
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
																	"/")), true);
				}
				catch(Exception exc)
                {

                }

				shows.add(
						new Show(
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
								date, parser.getValue(e, "Actors"), profile));

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
													"id")), profile, seriesId));

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
						String url =parser.getValue(e, "Image");
					image = Tools.DownloadFromUrl(
							"http://thetvdb.com/banners/"
									+ url,
							extStorage
									+ "/TVST/actors"
									+ url.substring(url.lastIndexOf("/")), true);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					

						actors.add(
								new Actor(
										parser.getValue(e, "id"),
										parser.getValue(e, "Name"),
										parser.getValue(e, "Role"),
										image,
										profile, seriesId));

				}
			}
            db.insertActors(actors);
            db.insertEpisodes(episodes);
            db.insertShows(shows);
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
	}

	@Override
	public void onDestroy()
	{
		Tools.setKeepScreenOn(MainActivity.this, false);
		db.close();
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

}