package rs.pedjaapps.tvshowtracker.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.fragment.ShowGridFragment;
import rs.pedjaapps.tvshowtracker.fragment.MyShowsFragment;

/**
 * Created by pedja on 31.5.14..
 */
public class PrefsManager
{
    public static final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());

    public static final String defaultUser = "-";

    public enum Key
    {
        locale, username, avatar, first_name, last_name, first_run,
        sort
    }

    public static String getLocale()
    {
        return prefs.getString(Key.locale.toString(), "en");
    }

    public static String getActiveUser()
    {
        return prefs.getString(Key.username.toString(), defaultUser);
    }

    public static void setActiveUser(String email)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.username.toString(), email);
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
}
