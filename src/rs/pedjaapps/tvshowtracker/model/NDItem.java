package rs.pedjaapps.tvshowtracker.model;

public class NDItem
{
	public static final int TYPE_MAIN = 0;
	public static final int TYPE_OPT = 1;
	public static final int TYPE_SEPARATOR = 2;

	public enum Id
	{
		undefined, trending, login_logout, my_watchlist, settings, my_shows
	}
	
	public String title;
	public int iconRes, type;
	public Id id = Id.undefined;
}
