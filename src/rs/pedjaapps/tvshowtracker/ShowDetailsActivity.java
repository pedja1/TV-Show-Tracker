package rs.pedjaapps.tvshowtracker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import com.viewpagerindicator.LinePageIndicator;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.Date;
import java.util.List;
import rs.pedjaapps.tvshowtracker.adapter.ShowDetailsPagerAdapter;
import rs.pedjaapps.tvshowtracker.model.EpisodeItem;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class ShowDetailsActivity extends BaseActivity
{

	ShowDetailsPagerAdapter mShowDetailsPagerAdapter;
	ViewPager mViewPager;
	static int mTvdbId;
	static String profile;
    private Show show;

    public static String EXTRA_TVDB_ID = "tvdb_id";
    ProgressBar pbLoading;
    //RelativeLayout rlDetailsContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		getActionBar().setBackgroundDrawable(/*getResources().getDrawable(R.drawable.glass_background)*/new ColorDrawable(Color.TRANSPARENT));
		
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        //rlDetailsContainer = (RelativeLayout)findViewById(R.id.rlDetailsContainer);

        // Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mTvdbId = getIntent().getIntExtra(EXTRA_TVDB_ID, 0);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		new ATLoadShow().execute();

	}

	public class ATLoadShow extends AsyncTask<String, Void, Show>
	{

		@Override
		protected Show doInBackground(String... args)
		{
            ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
            QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
            queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(mTvdbId));
			return queryBuilder.unique();
		}

		@Override
		protected void onPreExecute()
		{
			pbLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Show show)
		{
            ShowDetailsActivity.this.show = show;
            pbLoading.setVisibility(View.GONE);
			if(show == null)
            {
                Utility.showMessageAlertDialog(ShowDetailsActivity.this, getString(R.string.error_loading_show), null, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();
                    }
                });
            }
            else
            {
                setupViews();
            }
		}
	}
	
	private void setupViews()
	{
        getActionBar().setTitle(show.getTitle());
        /*ImageLoader.getInstance().loadImage(show.getImage().getPoster(), new SimpleImageLoadingListener()
        {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                ImageView ivBackground = (ImageView)findViewById(R.id.ivBackground);
                ivBackground.setImageBitmap(loadedImage);
                ivBackground.getDrawable().setAlpha(20);
            }
        });*/
		mShowDetailsPagerAdapter = new ShowDetailsPagerAdapter(getSupportFragmentManager(), this);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mShowDetailsPagerAdapter);
        LinePageIndicator linePageIndicator = (LinePageIndicator)findViewById(R.id.lines);
        linePageIndicator.setViewPager(mViewPager);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        //int page = prefs.getInt("details_def_page", 0);
        //mViewPager.setCurrentItem(page, false);
		linePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (position == 1)
                {
                    //OverviewFragment.setProgress();
                }
                setSubtitleByPosition(position);
            }
        });
        setSubtitleByPosition(0);
	}

    public void setSubtitleByPosition(int pos)
    {
        switch (pos)
        {
            case 0:
                getActionBar().setSubtitle(R.string.overview);
                break;
            case 1:
                getActionBar().setSubtitle(R.string.episodes);
                break;
            case 2:
                getActionBar().setSubtitle(R.string.actors);
                break;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(0, 0, 0, "Download Banner").setShowAsAction(
		//		MenuItem.SHOW_AS_ACTION_NEVER);
		/*
		 * menu.add(0, 1, 1, "Download Header")
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		 */
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case 0:
			//startActivity(new Intent(this, BannerActivity.class)
			//		.putExtra("seriesId", /*seriesId + */"")
			//		.putExtra("type", "banner").putExtra("profile", profile));
			return true;
		case 1:
			//startActivity(new Intent(this, BannerActivity.class).putExtra(
			//		"seriesId", /*seriesId + */"").putExtra("type", "fanart"));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private static int getLastAiredEpisodePosition(
			List<EpisodeItem> episodeItems)
	{
		int n = episodeItems.size() - 1;
		for (int i = episodeItems.size() - 1; i > 0; i--)
		{
			// for(int i = 0; i<episodeItems.size(); i++){
			try
			{
				Date firstAired = Constants.df.parse(episodeItems.get(i)
						.getFirstAired());
				if (new Date().after(firstAired))
				{
					return n;
				}
				else
				{
					n--;
				}

			}
			catch (Exception ex)
			{
				n--;
			}
		}
		return -1;
	}

	private static int getNextEpisodePosition(List<EpisodeItem> episodeItems)
	{
		int n = 0;

		for (EpisodeItem episodeItem : episodeItems)
		{
			try
			{
				Date firstAired = Constants.df.parse(episodeItem
						.getFirstAired());
				if (new Date().before(firstAired)
						|| (new Date().getTime() / (1000 * 60 * 60 * 24)) == (firstAired
								.getTime() / (1000 * 60 * 60 * 24)))
				{
					return n;
				}
				else
				{
					n++;
				}

			}
			catch (Exception ex)
			{
				n++;
			}
		}
		return -1;
	}
	
	public void onDestroy()
	{
		super.onDestroy();
	}

    public Show getShow()
    {
        return show;
    }
}
