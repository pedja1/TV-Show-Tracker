package rs.pedjaapps.tvshowtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.fragment.ShowGridFragment;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;
import rs.pedjaapps.tvshowtracker.network.SyncPolicy;

/**
 * Created by pedja on 31.5.14..
 */
public class PrefsManager
{
    public static final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());
    public static final SharedPreferences syncPolicyPrefs = MainApp.getContext().getSharedPreferences("sync_policy", Context.MODE_PRIVATE);

    public static final String defaultUser = "-";

    public enum Key
    {
        locale, username, avatar, first_name, last_name, first_run,
        sort, last_trakt_sync_ts, last_show_sync_ts, min_required_net_state, sync_interval_ms
    }

    public static String getLocale()
    {
        return prefs.getString(Key.locale.toString(), "en");
    }

    public static String getActiveUser()
    {
        return prefs.getString(Key.username.toString(), defaultUser);
    }

    public static void setActiveUser(String username)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.username.toString(), username);
        editor.apply();
    }

    public static MyShowsFragment.SortOrder getSortOrder()
    {
        MyShowsFragment.SortOrder sortOrder = MyShowsFragment.SortOrder.valueOf(prefs.getString(Key.sort.toString(), MyShowsFragment.SortOrder.id.toString()));
        if(sortOrder == null) sortOrder = MyShowsFragment.SortOrder.id;
        return sortOrder;
    }

    public static void setSortOrder(String sortOrder)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.sort.toString(), sortOrder);
        editor.apply();
    }

    public static boolean isFirstRun()
    {
        return prefs.getBoolean(Key.first_run.toString(), true);
    }

    public static void setFirstRun(boolean firstRun)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.first_run.toString(), firstRun);
        editor.apply();
    }

    public static long getLastTraktSyncTs()
    {
        return prefs.getLong(Key.last_trakt_sync_ts.toString(), 0);
    }

    public static void setLastTraktSyncTs(Date lastTraktSyncTs)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Key.last_trakt_sync_ts.toString(), lastTraktSyncTs.getTime());
        editor.apply();
    }

    public static SyncPolicy getSyncPolicy()
    {
        SyncPolicy policy = new SyncPolicy();
        policy.minRequiredNetState = syncPolicyPrefs.getInt(Key.min_required_net_state.toString(), SyncPolicy.DEFAULT_STATE);
        policy.syncIntervalMs = syncPolicyPrefs.getLong(Key.sync_interval_ms.toString(), SyncPolicy.DEFAULT_SYNC_INTERVAL);
        return policy;
    }

    public static void setSyncPolicy(SyncPolicy syncPolicy)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Key.sync_interval_ms.toString(), syncPolicy.syncIntervalMs);
        editor.putInt(Key.min_required_net_state.toString(), syncPolicy.minRequiredNetState);
        editor.apply();
    }
}
