package rs.pedjaapps.tvshowtracker.network;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import de.greenrobot.dao.query.QueryBuilder;
import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;
import rs.pedjaapps.tvshowtracker.fragment.ShowGridFragment;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.ShowMemCache;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class ShowWorkerService extends IntentService
{
    public static String INTENT_ACTION_DOWNLOAD_SHOW = "intent_action_download_show";
    public static String INTENT_EXTRA_TVDB_ID = "intent_extra_tvdb_id";
    public static String INTENT_EXTRA_FORCE_DOWNLOAD = "intent_extra_force_download";

    Handler handler;

    public ShowWorkerService()
    {
        super("show_worker_thread");
        handler = new Handler();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(intent != null && intent.getAction() != null)
        {
            String action = intent.getAction();
            if (INTENT_ACTION_DOWNLOAD_SHOW.equals(action))
            {
                long tvdbId = intent.getLongExtra(INTENT_EXTRA_TVDB_ID, -1);
                boolean forceDownload = intent.getBooleanExtra(INTENT_EXTRA_FORCE_DOWNLOAD, false);
                if (tvdbId != -1)
                {
                    ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
                    QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
                    queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(tvdbId));

                    if(queryBuilder.unique() == null)
                    {
                        final Show show = getShow(tvdbId, forceDownload);
                        Utility.addShowToDb(show);
                        ShowGridFragment.sendRefreshBroadcast(MyShowsFragment.LIST_TYPE);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Utility.showToast(ShowWorkerService.this, getString(R.string.show_downloaded, show.getTitle()));
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Download show from net
     * It will first check mem cache*/
    public static Show getShow(long tvdbId, boolean forceDownload)
    {
        Show cachedShow = ShowMemCache.getInstance().getCachedShow(tvdbId + "");
        if(cachedShow == null || forceDownload)
        {
            JSONUtility.Response response = JSONUtility.parseShow(tvdbId + "", false);
            return response.getShow();
        }
        return cachedShow;
    }

  /*  public void downloadingNotification(String show)
    {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent dataIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(dataIntent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        //maybe type icon(message icon for example if its message)
        mBuilder.setSmallIcon(R.drawable.ic_stat_download);

        mBuilder.setContentTitle("Downloading Shows");

        //mBuilder.setStyle(getNotificationStyle(pushNotification));

        mBuilder.setContentText(Html.fromHtml(getContentText(pushNotification)));
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.FLAG_ONLY_ALERT_ONCE);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(getNotificationId(pushNotification.getType()), mBuilder.build());
    }*/

}
