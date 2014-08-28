package rs.pedjaapps.tvshowtracker.model;

public class NDItem
{
	public static final int TYPE_MAIN = 0;
	public static final int TYPE_OPT = 1;
	
	public enum Id
	{
		undefined, trending, my_shows
	}
	
	public String title;
	public int iconRes, type;
	public Id id = Id.undefined;
}
