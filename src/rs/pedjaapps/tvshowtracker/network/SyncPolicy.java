package rs.pedjaapps.tvshowtracker.network;

/**
 * Created by pedja on 2.11.14..
 */
public class SyncPolicy
{
    public static final long DEFAULT_SYNC_INTERVAL = 1000 * 60 * 60 * 24;
    public static final int DEFAULT_STATE = Network.NETWORK_STATE_WIFI;

    public int minRequiredNetState = DEFAULT_STATE;
    public long syncIntervalMs = DEFAULT_SYNC_INTERVAL;

    public SyncPolicy(int minRequiredNetState, long syncIntervalMs)
    {
        this.minRequiredNetState = minRequiredNetState;
        this.syncIntervalMs = syncIntervalMs;
    }

    public SyncPolicy()
    {
    }
}
