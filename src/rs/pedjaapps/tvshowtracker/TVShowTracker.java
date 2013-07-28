package rs.pedjaapps.tvshowtracker;

import android.app.Application;
import android.content.Context;

public class TVShowTracker extends Application {

    private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
        TVShowTracker.context = getApplicationContext();
	}

    public static Context getAppContext()
    {
        return TVShowTracker.context;
    }
}
