package rs.pedjaapps.tvshowtracker.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rs.pedjaapps.tvshowtracker.MainApp;

/**
 * @author Predrag Čokulov*/

public class Network
{
    public static final int NETWORK_STATE_OFFLINE = 0;
    public static final int NETWORK_STATE_3G = 1;
    public static final int NETWORK_STATE_WIFI = 2;
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static boolean isWiFiConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi != null && mWifi.isConnected();
    }

    public static int getNetworkState()
    {
        if(Network.isWiFiConnected(MainApp.getContext()))
        {
            return NETWORK_STATE_WIFI;
        }
        else if(Network.isNetworkAvailable(MainApp.getContext()))
        {
            return NETWORK_STATE_3G;
        }
        else
        {
            return NETWORK_STATE_OFFLINE;
        }
    }
}
