package rs.pedjaapps.trakttvandroid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import de.greenrobot.dao.D

public class MainApp extends Application {
	
	static Context context;
        DevOpenHelper dbHelper;
        SQLiteDatabase db;
        DaoMaster daoMaster;

	public static Context getContext()
	{
		return context;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		initImageLoader(getApplicationContext());
                initDb();
	}

	public static void initImageLoader(Context context) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit 
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.memoryCacheSize(memoryCacheSize)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.enableLogging()// Not necessary in common
			.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

    private void initDb() 
    {
        dbHelper = new DaoMaster.DevOpenHelper(this, "trakttv.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
    }
}
