package rs.pedjaapps.tvshowtracker;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class ShowDetailsActivity extends BaseActivity implements Drawable.Callback
{

	private static final int SD_RTL = 0;
	private static final int SD_LTR = 1;
	private static final int SD_NONE = -1;
	
	int swipeDirection = SD_NONE;
	private ScrollDirectionTracker scrollDirectionTracker;
	
	ShowDetailsPagerAdapter mShowDetailsPagerAdapter;
	ViewPager mViewPager;
	private int mTvdbId;
    private String mImdbId;
    private Show show;
    private boolean isInMyShows;

    public static final String EXTRA_TVDB_ID = "tvdb_id";
    public static final String EXTRA_IMDB_ID = "imdb_id";

    ProgressBar pbLoading;

    private Drawable mActionBarBackgroundDrawable;
	int mActionBarAlpha = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY | Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            mActionBarBackgroundDrawable.setCallback(this);
        }

        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab_background_textured_tvst_red).mutate();
        mActionBarBackgroundDrawable.setAlpha(mActionBarAlpha);
		
		getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
		
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        // Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mTvdbId = getIntent().getIntExtra(EXTRA_TVDB_ID, -1);
        mImdbId = getIntent().getStringExtra(EXTRA_IMDB_ID);

        if(mTvdbId == -1 && mImdbId == null)
        {
            //TODO show error and
            return;
        }

		new ATLoadShow().execute();
		
	}

    @Override
    public void invalidateDrawable(Drawable who)
    {
        getActionBar().setBackgroundDrawable(who);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when)
    {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what)
    {

    }

    public class ATLoadShow extends AsyncTask<String, Void, Show>
	{
        boolean isTvdb;

        public ATLoadShow()
        {
            isTvdb = mTvdbId != -1;
        }

        @Override
		protected Show doInBackground(String... args)
		{
            ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
            QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
            if (isTvdb)
            {
                //try db first
                queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(mTvdbId));
                Show show = queryBuilder.unique();
                //no show in db, download
                if(show == null)
                {
                    JSONUtility.Response response = JSONUtility.parseShow(mTvdbId + "", false);
                    return response.getShow();
                }
                else
                {
                    isInMyShows = true;
                    return show;
                }
            }
            else
            {
                queryBuilder.where(ShowDao.Properties.Imdb_id.eq(mImdbId));
                Show show = queryBuilder.unique();
                //no show in db, download
                if(show == null)
                {
                    JSONUtility.Response response = JSONUtility.parseShow(mTvdbId + "", false);
                    return response.getShow();
                }
                else
                {
                    isInMyShows = true;
                    return show;
                }
            }
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
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mShowDetailsPagerAdapter);
        LinePageIndicator linePageIndicator = (LinePageIndicator)findViewById(R.id.lines);
        linePageIndicator.setViewPager(mViewPager);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        //int page = prefs.getInt("details_def_page", 0);
        //mViewPager.setCurrentItem(page, false);
		linePageIndicator.setOnPageChangeListener(new PageChangeListener());
        setSubtitleByPosition(0);
	}
	
	private class PageChangeListener implements ViewPager.OnPageChangeListener
	{
		
		int currentPage = 0;
		
		public PageChangeListener()
		{
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			if(arg0 == ViewPager.SCROLL_STATE_IDLE)
			{
				currentPage = mViewPager.getCurrentItem();
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel)
		{
			//System.out.println("scrollState " + scrollState);
			
			//System.out.println("position: " + position + ", positionOffset: " + positionOffset);
			int scrollPercent = (int) (positionOffset * 100);
			int alphaDiff = 255 - mActionBarAlpha;
			int screenWidth = DisplayManager.screenWidth;
			//System.out.println("onPageScrolled " + newPage);
			if(swipeDirection == SD_LTR && currentPage == 1)
			{
				System.out.println("swiping left to right");
                final int newAlpha = (alphaDiff * scrollPercent / 100) + mActionBarAlpha;
				System.out.println(alphaDiff + " " + newAlpha + " " + scrollPercent);
				mActionBarBackgroundDrawable.setAlpha(newAlpha);
			}
			else if(swipeDirection == SD_RTL && currentPage == 0)
			{
				System.out.println("swiping right to left");
				final int newAlpha = (alphaDiff * scrollPercent / 100 ) + mActionBarAlpha;
				System.out.println(alphaDiff + " " + newAlpha + " " + scrollPercent);
				if(scrollPercent > 0)mActionBarBackgroundDrawable.setAlpha(newAlpha);
			}
		}

		@Override
		public void onPageSelected(int position)
		{
			System.out.println("onPageSelected" + position);

			if (position == 0)
			{
				//OverviewFragment.setProgress();
				//mActionBarBackgroundDrawable.setAlpha(mActionBarAlpha);
			}
			else
			{
				//mActionBarBackgroundDrawable.setAlpha(255);
			}
			setSubtitleByPosition(position);
		}
		
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch(ev.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
				scrollDirectionTracker = new ScrollDirectionTracker(ev.getX());
				break;
			case MotionEvent.ACTION_MOVE:
				if(scrollDirectionTracker.firstX < ev.getX())
				{
					swipeDirection = SD_LTR;
				}
				else if(scrollDirectionTracker.firstX > ev.getX())
				{
					swipeDirection = SD_RTL;
				}
				else
				{
					swipeDirection = SD_NONE;
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private class ScrollDirectionTracker
	{
		float firstX;

		public ScrollDirectionTracker(float firstX)
		{
			this.firstX = firstX;
		}
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

    public Drawable getActionBarBackgroundDrawable()
    {
        return mActionBarBackgroundDrawable;
    }
	
	public void setActionBarAlpha(int mActionBarAlpha)
	{
		this.mActionBarAlpha = mActionBarAlpha;
	}

	public int getActionBarAlpha()
	{
		return mActionBarAlpha;
	}
	
}
