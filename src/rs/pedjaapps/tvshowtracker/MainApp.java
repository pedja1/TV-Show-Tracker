package rs.pedjaapps.tvshowtracker;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.cache.DiskLruBasedCache;

import rs.pedjaapps.tvshowtracker.model.DaoMaster;
import rs.pedjaapps.tvshowtracker.model.DaoSession;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;

public class MainApp extends Application
{
    private static MainApp mainApp = null;

    static Context context;
    DaoSession daoSession;
    private String activeUser;
    public DiskLruBasedCache.ImageCacheParams cacheParams;

    public static MainApp getInstance()
    {
        if(mainApp == null)mainApp = new MainApp();
        return mainApp;
    }

    public static Context getContext()
    {
        return context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
		//Crashlytics.start(this);
        initImageLoader();
        mainApp = this;
        activeUser = initUser();
    }

    private String initUser()
    {
        return PrefsManager.getActiveUser();
    }

    public String getActiveUser()
    {
        if(activeUser == null)
        {
            activeUser = initUser();
        }
        return activeUser;
    }

    public void setActiveUser(String activeUser)
    {
        this.activeUser = activeUser;
    }

    public void initImageLoader()
    {
        cacheParams = new DiskLruBasedCache.ImageCacheParams(getContext().getApplicationContext(), Constants.CACHE_FOLDER_NAME);
        cacheParams.setMemCacheSizePercent(0.2f);
        cacheParams.diskCacheSize = 1024 * 1024 * 200;//200MB, is it ot much?
        cacheParams.diskCacheEnabled = true;
        cacheParams.memoryCacheEnabled = false;
    }

    public DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_TABLE_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
