package rs.pedjaapps.tvshowtracker.model;

public class NDItem
{
	public static final int TYPE_MAIN = 0;
	public static final int TYPE_OPT = 1;
	public static final int TYPE_SEPARATOR = 2;

	public enum Id
	{
		undefined, movies, shows, login_logout, settings, calendar, recommendations
	}
	
	public String title;
	public int type;
	public Id id = Id.undefined;
}
