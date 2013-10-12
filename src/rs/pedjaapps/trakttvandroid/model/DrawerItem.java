package rs.pedjaapps.trakttvandroid.model;

public class DrawerItem
{
    private String text;
	private int resId;

	public DrawerItem(String text, int resId)
	{
		this.text = text;
		this.resId = resId;
	}
	
	public String getText()
	{
		return text;
	}

	public int getResId()
	{
		return resId;
	}
	
}
