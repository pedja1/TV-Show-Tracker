package rs.pedjaapps.tvshowtracker.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import rs.pedjaapps.tvshowtracker.MainApp;

public class DisplayManager
{
	public static final int screenWidth;
    public static final int screenHeight;
	
	static
	{
		WindowManager wm = (WindowManager) MainApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }
        else
        {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }
	}
}
