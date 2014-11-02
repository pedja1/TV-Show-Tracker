package rs.pedjaapps.tvshowtracker.fragment;

import java.util.List;

import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.ShowMemCache;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class TrendingShowsFragment extends ShowGridFragment
{
    public static final int LIST_TYPE = 1002;
    String noShowsMessage;

    public static TrendingShowsFragment newInstance()
    {
        TrendingShowsFragment fragment = new TrendingShowsFragment();
        return fragment;
    }

    @Override
    protected List<Show> getShows()
    {
        List<Show> shows = ShowMemCache.getInstance().getCachedList(ShowMemCache.ListKey.trending);
        if (shows == null)
        {
            System.out.println("get from net");
            JSONUtility.Response response = JSONUtility.parseTrendingShows();
            if (response.getStatus())
            {
				shows = response.getShowList();
                ShowMemCache.getInstance().addListToCache(ShowMemCache.ListKey.trending, shows);
				Utility.calculateUpcomingEpisodes(shows);
                return shows;
            }
            else
            {
                noShowsMessage = response.getErrorCode().toString();
                return null;
            }
        }
        else
        {
            System.out.println("cache hit");
			Utility.calculateUpcomingEpisodes(shows);
            return shows;
        }
    }

    @Override
    protected CharSequence noShowsString()
    {
        return noShowsMessage;
    }

    @Override
    protected int listType()
    {
        return LIST_TYPE;
    }

}
