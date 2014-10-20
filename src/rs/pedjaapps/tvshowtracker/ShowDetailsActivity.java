package rs.pedjaapps.tvshowtracker;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.viewpagerindicator.LinePageIndicator;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import rs.pedjaapps.tvshowtracker.adapter.ShowDetailsPagerAdapter;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.EpisodeItem;
import rs.pedjaapps.tvshowtracker.model.Genre;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;
import rs.pedjaapps.tvshowtracker.utils.ShowMemCache;
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
    private String mImdbId, showName;
    private Show show;
    private boolean isInMyShows;

    public static final String EXTRA_TVDB_ID = "tvdb_id";
    public static final String EXTRA_IMDB_ID = "imdb_id";
    public static final String EXTRA_SHOW_NAME = "show_name";

    ProgressBar pbLoading;

    private Drawable mActionBarBackgroundDrawable;
	int mActionBarAlpha = 0;
	
	private ATLoadShow atLoadShow;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY | Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY | Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		getSupportActionBar().setTitle("");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            mActionBarBackgroundDrawable.setCallback(this);
        }

        mActionBarBackgroundDrawable = new ColorDrawable(getResources().getColor(R.color.primary)).mutate();
        
		getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
		
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        // Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mTvdbId = getIntent().getIntExtra(EXTRA_TVDB_ID, -1);
        mImdbId = getIntent().getStringExtra(EXTRA_IMDB_ID);
        showName = getIntent().getStringExtra(EXTRA_SHOW_NAME);

        if(mTvdbId == -1 && mImdbId == null && showName == null)
        {
            //TODO show error and
            return;
        }

		atLoadShow = new ATLoadShow();
		atLoadShow.execute();
		
	}

    @Override
    public void invalidateDrawable(Drawable who)
    {
        getSupportActionBar().setBackgroundDrawable(who);
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

        @Override
		protected Show doInBackground(String... args)
		{
			ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
			QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
			
            Show cachedShow = ShowMemCache.getInstance().getCachedShow(mTvdbId == -1 ? (mImdbId == null ? showName : mImdbId) : mTvdbId + "");
            if(cachedShow == null)
            {
                if (mTvdbId != -1)
                {
                    //try db first
                    queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(mTvdbId));
                    Show show = queryBuilder.unique();
                    //no show in db, download
                    if (show == null)
                    {
                        JSONUtility.Response response = JSONUtility.parseShow(mTvdbId + "", false);
                        ShowMemCache.getInstance().addShowToCache(mTvdbId + "", response.getShow());
                        return response.getShow();
                    }
                    else
                    {
                        isInMyShows = true;
                        return show;
                    }
                }
                else if (showName != null)
                {
                    //cant query db by name
                    JSONUtility.Response response = JSONUtility.parseShow(showName, false);
                    ShowMemCache.getInstance().addShowToCache(showName, response.getShow());
                    return response.getShow();
                }
                else
                {
                    queryBuilder.where(ShowDao.Properties.Imdb_id.eq(mImdbId));
                    Show show = queryBuilder.unique();
                    //no show in db, download
                    if (show == null)
                    {
                        JSONUtility.Response response = JSONUtility.parseShow(mImdbId + "", false);
                        ShowMemCache.getInstance().addShowToCache(mImdbId, response.getShow());
                        return response.getShow();
                    }
                    else
                    {
                        isInMyShows = true;
                        return show;
                    }
                }
            }
            else
            {
				//check db anyway
				queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(cachedShow.getTvdb_id()));
				Show show = queryBuilder.unique();
				if(show != null)
				{
					isInMyShows = true;
					return show;
				}
                return cachedShow;
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
		mActionBarBackgroundDrawable.setAlpha(mActionBarAlpha);

		invalidateOptionsMenu();
        getSupportActionBar().setTitle(show.getTitle());
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
                getSupportActionBar().setSubtitle(R.string.overview);
                break;
            case 1:
                getSupportActionBar().setSubtitle(R.string.episodes);
                break;
            case 2:
                getSupportActionBar().setSubtitle(R.string.actors);
                break;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add(0, 0, 0, R.string.add_to_fav);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		item.setIcon(R.drawable.ic_action_not_fav);
		item.setVisible(false);
		/*
		 * menu.add(0, 1, 1, "Download Header")
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		 */
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem item = menu.findItem(0);
		item.setIcon(isInMyShows ? R.drawable.ic_action_fav : R.drawable.ic_action_not_fav);
		item.setTitle(isInMyShows ? R.string.remove_from_fav : R.string.add_to_fav);
		item.setVisible(show != null);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case 0:
			if(isInMyShows)
			{
				removeShowFromDb();
			}
			else
			{
				addShowToDb();
			}
			return true;
		case 1:
			//startActivity(new Intent(this, BannerActivity.class).putExtra(
			//		"seriesId", /*seriesId + */"").putExtra("type", "fanart"));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void removeShowFromDb()
	{
		Utility.deleteShowFromDb(show);
		
		isInMyShows = false;
		invalidateOptionsMenu();
		Utility.showToast(this, R.string.show_removeed_from_fav);
	}

	private void addShowToDb()
	{
		ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
		QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
		queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(show.getTvdb_id()));
		
		if(queryBuilder.unique() != null)
		{
			isInMyShows = true;
			invalidateOptionsMenu();
			return;
		}
		
		ImageDao imageDao = MainApp.getInstance().getDaoSession().getImageDao();
		long imageId = imageDao.insertOrReplace(show.getImage());
		show.setImage_id(imageId);
        show.setUsername(MainApp.getInstance().getActiveUser().getUsername());
		long showId = showDao.insertOrReplace(show);
		
		EpisodeDao episodeDao = MainApp.getInstance().getDaoSession().getEpisodeDao();
		for(Episode e : show.getEpisodes())
		{
			e.setShow_id(showId);
		}
		episodeDao.insertOrReplaceInTx(show.getEpisodes());
		
		ActorDao actorDao = MainApp.getInstance().getDaoSession().getActorDao();
		for(Actor a : show.getActors())
		{
			a.setShow_id(showId);
		}
		actorDao.insertOrReplaceInTx(show.getActors());
		
		GenreDao gDao = MainApp.getInstance().getDaoSession().getGenreDao();
		for(Genre g : show.getGenres())
		{
			g.setShow_id(showId);
		}
		gDao.insertOrReplaceInTx(show.getGenres());
		
		isInMyShows = true;
		invalidateOptionsMenu();
		Utility.showToast(this, R.string.show_added_to_fav);
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
		if(atLoadShow != null)atLoadShow.cancel(true);
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
