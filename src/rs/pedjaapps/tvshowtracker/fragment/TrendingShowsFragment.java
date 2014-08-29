package rs.pedjaapps.tvshowtracker.fragment;
import java.util.List;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;

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
		JSONUtility.Response response = JSONUtility.parseTrendingShows();
		if(response.getStatus())
		{
			return response.getShowList();
		}
		else
		{
			noShowsMessage = response.getErrorCode().toString();
			return null;
		}
	}

	@Override
	protected CharSequence noShowsString()
	{
		return noShowsMessage;
	}
	
}
