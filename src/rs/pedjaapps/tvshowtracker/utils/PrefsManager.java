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
        locale, email
    }

    public static String getLocale()
    {
        return prefs.getString(Key.locale.toString(), "en");
    }

    public static String getActiveUserEmail()
    {
        return prefs.getString(Key.email.toString(), defaultUser);
    }
}
