package rs.pedjaapps.tvshowtracker.network;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;
import rs.pedjaapps.tvshowtracker.fragment.ShowGridFragment;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.SyncLog;
import rs.pedjaapps.tvshowtracker.model.SyncLogDao;
import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;


public class TraktSyncService extends IntentService
{

    private static final int ONGOING_NOTIFICATION_ID = 1001;
    private static final String INTENT_EXTRA_FORCE_SYNC = "force_sync";

    public TraktSyncService()
    {
        super("TraktSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        boolean forceSync = intent != null && intent.getBooleanExtra(INTENT_EXTRA_FORCE_SYNC, false);
        SyncPolicy syncPolicy = PrefsManager.getSyncPolicy();
        if(!forceSync && (syncPolicy.syncIntervalMs + PrefsManager.getLastTraktSyncTs() < new Date().getTime()
                || Network.getNetworkState() < syncPolicy.minRequiredNetState))
        {
            return;
        }

        Intent dataIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(dataIntent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_sync));

        mBuilder.setContentTitle(getString(R.string.synchronising_shows));

        //mBuilder.setContentText("-");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setProgress(0, 0, true);

        startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());

        SyncLogDao syncLogDao = MainApp.getInstance().getDaoSession().getSyncLogDao();
        List<SyncLog> syncLogs = new ArrayList<>();

        JSONUtility.Response response = JSONUtility.parseCollectionResponse();
        if (!response.getStatus())
        {
            syncLogs.add(new SyncLog(response.getErrorCode().toString(), response.getErrorMessage(), SyncLog.TYPE_GENERAL, null));
            syncLogDao.insertInTx(syncLogs);
            stopForeground(true);
            return;
        }
        List<Show> trakt = response.getShowList();
        List<Show> local = MainApp.getInstance().getActiveUser().getShows();

        /*List<Show> common = new ArrayList<>(trakt);
        common.retainAll(local);
        //common now contains duplicate elements

        for(Show show : common)
        {
            trakt.remove(show);
            local.remove(show);
        }*/
        //both lists now contain uniue elements

        int maxProgress = trakt.size() + local.size() + 1;
        int progress = 1;

        //first download from trakt
        for (Show traktShow : trakt)
        {
            mBuilder.setProgress(progress, maxProgress, false);
            mBuilder.setContentText(traktShow.getTitle());
            startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());

            EpisodeDao episodeDao = MainApp.getInstance().getDaoSession().getEpisodeDao();

            Show localShow = Show.findShowWithId(traktShow.getTvdb_id(), local);
            if (localShow == null)//show doesn't exist locally, download it
            {
                Show fullShow = ShowWorkerService.getShow(traktShow.getTvdb_id(), false);
                if(fullShow != null)
                {
                    Utility.addShowToDb(fullShow);
                    episodeDao.insertOrReplaceInTx(Show.findEpisodes(fullShow.getEpisodes(), traktShow.getEpisodes()));
                    syncLogs.add(new SyncLog("success", null, SyncLog.TYPE_DOWN, fullShow.getTitle()));
                }
                else
                {
                    syncLogs.add(new SyncLog("failure", null, SyncLog.TYPE_DOWN, traktShow.getTitle()));
                }
            }
            else
            {
                episodeDao.insertOrReplaceInTx(Show.findEpisodes(localShow.getEpisodes(), traktShow.getEpisodes()));
                syncLogs.add(new SyncLog("success", null, SyncLog.TYPE_DOWN, localShow.getTitle()));
            }

            progress++;
        }

        //upload to trakt
        for(Show localShow : local)
        {
            mBuilder.setProgress(progress, maxProgress, false);
            mBuilder.setContentText(localShow.getTitle());
            startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());

            Show traktShow = Show.findShowWithId(localShow.getTvdb_id(), trakt);
            if (traktShow == null)//show doesn't exist on trakt, add it
            {
                JSONUtility.Response addResponse = JSONUtility.addToLibrary(localShow);
                if(addResponse.getStatus())
                {
                    syncLogs.add(new SyncLog(addResponse.getErrorCode().toString(), addResponse.getErrorMessage(), SyncLog.TYPE_UP, localShow.getTitle()));
                }
                else
                {
                    syncLogs.add(new SyncLog(addResponse.getErrorCode().toString(), addResponse.getErrorMessage(), SyncLog.TYPE_UP, localShow.getTitle()));
                }
            }
            else
            {
                List<Episode> episodes = Show.findEpisodes(traktShow.getEpisodes(), localShow.getEpisodes());
                if (!episodes.isEmpty())//if there are diff in local nad trakt episodes, upload
                {
                    JSONUtility.Response addResponse = JSONUtility.addToLibrary(localShow);
                    if(addResponse.getStatus())
                    {
                        syncLogs.add(new SyncLog(addResponse.getErrorCode().toString(), addResponse.getErrorMessage(), SyncLog.TYPE_UP, localShow.getTitle()));
                    }
                    else
                    {
                        syncLogs.add(new SyncLog(addResponse.getErrorCode().toString(), addResponse.getErrorMessage(), SyncLog.TYPE_UP, localShow.getTitle()));
                    }
                }
            }

            progress++;
        }

        UserDao userDao = MainApp.getInstance().getDaoSession().getUserDao();
        userDao.loadAll();
        syncLogDao.insertInTx(syncLogs);

        ShowGridFragment.sendRefreshBroadcast(MyShowsFragment.LIST_TYPE);

        PrefsManager.setLastTraktSyncTs(new Date());

        stopForeground(true);
    }
}
