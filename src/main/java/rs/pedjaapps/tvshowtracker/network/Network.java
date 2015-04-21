package rs.pedjaapps.tvshowtracker.network;

import android.content.*;
import android.net.*;
import rs.pedjaapps.tvshowtracker.*;


/**
 * @author Predrag Čokulov*/

public class Network
{
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

    public static NETWORK_STATE getNetworkState()
    {
        if(Network.isWiFiConnected(MainApp.getContext()))
        {
            return NETWORK_STATE.ONLINE_WIFI;
        }
        else if(Network.isNetworkAvailable(MainApp.getContext()))
        {
            return NETWORK_STATE.ONLINE_3G;
        }
        else
        {
            return NETWORK_STATE.OFFLINE;
        }
    }

    public enum NETWORK_STATE
    {
        ONLINE_3G, ONLINE_WIFI, OFFLINE
    }
}
