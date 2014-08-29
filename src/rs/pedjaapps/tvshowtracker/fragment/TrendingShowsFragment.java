package rs.pedjaapps.tvshowtracker.fragment;
import java.util.List;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.ShowMemCache;

public class TrendingShowsFragment extends ShowGridFragment
{
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
		if(shows == null)
		{
			System.out.println("get from net");
		JSONUtility.Response response = JSONUtility.parseTrendingShows();
		if(response.getStatus())
		{
			ShowMemCache.getInstance().addListToCache(ShowMemCache.ListKey.trending, response.getShowList());
			return response.getShowList();
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
			return shows;
		}
	}

	@Override
	protected CharSequence noShowsString()
	{
		return noShowsMessage;
	}
	
}
