package rs.pedjaapps.tvshowtracker.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;

/**
 * Created by pedja on 31.5.14..
 */
public class PrefsManager
{
    public static final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());

    public static final String defaultUser = "-";

    public enum Key
    {
        locale, email, avatar, first_name, last_name,
        sort
    }

    public static String getLocale()
    {
        return prefs.getString(Key.locale.toString(), "en");
    }

    public static String getActiveUser()
    {
        return prefs.getString(Key.email.toString(), defaultUser);
    }

    public static void setActiveUser(String email)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.email.toString(), email);
        editor.apply();
    }

    public static MainActivity.SortOrder getSortOrder()
    {
        MainActivity.SortOrder sortOrder = MainActivity.SortOrder.valueOf(prefs.getString(Key.sort.toString(), MainActivity.SortOrder.id.toString()));
        if(sortOrder == null) sortOrder = MainActivity.SortOrder.id;
        return sortOrder;
    }

    public static void setSortOrder(String sortOrder)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.sort.toString(), sortOrder);
        editor.apply();
    }
}
