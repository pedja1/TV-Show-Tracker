package rs.pedjaapps.tvshowtracker.fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PoppyViewHelper;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.Comparators;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class MyShowsFragment extends ShowGridFragment
{

	LinearLayout llUpcomingEpisode;
    Episode upcomingEpisode;
    TextView tvUpcomingEpisode;

	PoppyViewHelper poppyViewHelper;
	
	public static MyShowsFragment newInstance()
	{
		MyShowsFragment fragment = new MyShowsFragment();
		return fragment;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		poppyViewHelper = new PoppyViewHelper(getActivity(), PoppyViewHelper.PoppyViewPosition.BOTTOM);
        View poppyView = poppyViewHelper.createPoppyViewOnGridView(list, R.layout.upcoming_episode_layout);
		llUpcomingEpisode = (LinearLayout)poppyView.findViewById(R.id.llUpcomingEpisode);
        tvUpcomingEpisode = (TextView)poppyView.findViewById(R.id.tvUpcomingEpisode);
		
	}
	
	@Override
	protected CharSequence noShowsString()
	{
		String text = getString(R.string.no_shows_message);
        SpannableStringBuilder builder = new SpannableStringBuilder(Html.fromHtml(text));
        builder.setSpan(new ImageSpan(getActivity(), R.drawable.ic_action_search_dark), 22, 23, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      	return builder;
	}
	
	@Override
	protected List<Show> getShows()
    {
        long startTime = System.currentTimeMillis();
		SortOrder sort = PrefsManager.getSortOrder();
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
	protected void onShowsLoaded(List<Show> shows)
	{
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
	
	public enum SortOrder
    {
        id, show_name, next_episode, unwatched_episodes, network ,rating, runtime, status
	}
}
