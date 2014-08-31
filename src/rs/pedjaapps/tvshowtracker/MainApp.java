package rs.pedjaapps.tvshowtracker;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.cache.DiskLruBasedCache;

import de.greenrobot.dao.query.QueryBuilder;
import rs.pedjaapps.tvshowtracker.model.DaoMaster;
import rs.pedjaapps.tvshowtracker.model.DaoSession;
import rs.pedjaapps.tvshowtracker.model.User;
import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import com.android.volley.VolleyLog;

public class MainApp extends Application
{
    private static MainApp mainApp = null;

    static Context context;
    DaoSession daoSession;
    private User activeUser;
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

    private User initUser()
    {
        String username = PrefsManager.getActiveUser();
        UserDao userDao = getDaoSession().getUserDao();
        QueryBuilder<User> builder = userDao.queryBuilder();
        builder.where(UserDao.Properties.Username.eq(username));
        User user = builder.unique();
        if(user == null)
        {
            user = new User();
            user.setUsername("-");
            user.setAvatar("http://slurm.trakt.us/images/avatar-large.jpg");
            userDao.insert(user);
            return user;
        }
        return user;
    }

    public User getActiveUser()
    {
        if(activeUser == null)
        {
            activeUser = initUser();
        }
        return activeUser;
    }

    public void setActiveUser(User activeUser)
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
		VolleyLog.DEBUG = false;
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
