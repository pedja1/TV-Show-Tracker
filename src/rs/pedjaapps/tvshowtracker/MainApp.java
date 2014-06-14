package rs.pedjaapps.tvshowtracker;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import de.greenrobot.dao.query.QueryBuilder;
import rs.pedjaapps.tvshowtracker.model.DaoMaster;
import rs.pedjaapps.tvshowtracker.model.DaoSession;
import rs.pedjaapps.tvshowtracker.model.User;
import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;

public class MainApp extends Application
{
    private static MainApp mainApp = null;

    static Context context;
    DaoSession daoSession;
    private User activeUser;
    public DisplayImageOptions displayImageOptions;

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
        initImageLoader();
        mainApp = this;
        activeUser = initUser();
    }

    private User initUser()
    {
        String email = PrefsManager.getActiveUserEmail();
        if(email.equals(PrefsManager.defaultUser))
        {
            return createDefaultUser();
        }
        else
        {
            UserDao userDao = getDaoSession().getUserDao();
            QueryBuilder<User> queryBuilder = userDao.queryBuilder();
            queryBuilder.where(UserDao.Properties.Email.eq(email));
            User user = queryBuilder.build().unique();
            return user == null ? createDefaultUser() : user;
        }
    }

    private User createDefaultUser()
    {
        User user = new User();
        user.setEmail(PrefsManager.defaultUser);
        user.setFirst_name(getString(R.string._default));
        user.setLast_name(getString(R.string.user));
        user.setId(-1);
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
        displayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                //.showImageForEmptyUri(android.R.color.transparent)
                //.showImageOnFail(android.R.color.transparent)
                //.showImageOnLoading(android.R.color.transparent)
                //.resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //.threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(8)
                //.denyCacheImageMultipleSizesInMemory()
                //.discCacheFileNameGenerator(new Md5FileNameGenerator())
                //.tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
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
