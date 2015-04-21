package rs.pedjaapps.tvshowtracker.utils;

import java.text.SimpleDateFormat;

public class Constants
{
    public static final String CLIENT_ID = "21d99c083e81a2906244853ecec854b15ae2a50cc9387ce337f26165f1f10995";
    public static final String CLIENT_SECRET = "d5715cdcc16836378409d7c42f9821a19051fccc41d80bcd49cd81cbd7a5f7c5";
    public static final String API_HOST = "https://api-v2launch.trakt.tv/";
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
    public static final String LOG_TAG = "tvst";
    public static final String DB_TABLE_NAME = "tvst.db";
    public static final int CONN_TIMEOUT = 2 * 60 * 1000;//2 min
    public static final String TRAKT_URL = "http://trakt.tv";
    public static final String CACHE_FOLDER_NAME = ".cache";

    public static String ENCODING = "UTF-8";
}
