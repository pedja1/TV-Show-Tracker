package rs.pedjaapps.tvshowtracker.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.User;

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

    public static User getActiveUser()
    {
        User user = new User();
        user.setEmail(prefs.getString(Key.email.toString(), defaultUser));
        user.setAvatar(prefs.getString(Key.avatar.toString(), null));
        user.setFirst_name(prefs.getString(Key.first_name.toString(), null));
        user.setLast_name(prefs.getString(Key.last_name.toString(), null));
        return user;
    }

    public static void setActiveUser(User user)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.email.toString(), user.getEmail());
        editor.putString(Key.avatar.toString(), user.getAvatar());
        editor.putString(Key.first_name.toString(), user.getFirst_name());
        editor.putString(Key.locale.toString(), user.getLast_name());
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
