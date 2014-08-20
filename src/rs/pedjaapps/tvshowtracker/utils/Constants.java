package rs.pedjaapps.tvshowtracker.utils;

import java.text.SimpleDateFormat;

public class Constants
{
    public static final String TRAKT_API_KEY = "f18fce5c75ed1f25aef95c3626ba8935";
    public static final String REQUEST_URL_SEARCH = "http://api.trakt.tv/search/shows.json/" + TRAKT_API_KEY;
    public static final String REQUEST_URL_GET_SHOW_INFO = "http://api.trakt.tv/show/summary.json/" + TRAKT_API_KEY;
    public static final String IMDB_URL_PREFIX = "http://www.imdb.com/title/";
    public static final String REQUEST_URL_LOGIN = "http://www.pedjaapps.net/tvst/login.php";
    public static final String REQUEST_URL_REGISTER = "http://www.pedjaapps.net/tvst/register.php";

    public static final String apiKey = "E4BD239A1D7130F7";//tvdb key, not used anymore
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
    public static final int SHOW_DOWNLOAD_CODE = 000;
    public static final int SHOW_UPDATE_CODE = 111;
    public static final int UI_CODE_PRELOAD = 0;
    public static final int UI_CODE_AFTERLOAD = 1;
    public static final int REFRESH_HANDLER_UPDATE_LIST = 1001;
    public static final String LOG_TAG = "tvst";
    public static final String DB_TABLE_NAME = "tvst.db";
    public static final int CONN_TIMEOUT = 2 * 60 * 1000;//2 min
    public static final String TRAKT_URL = "http://trakt.tv";
    public static final String CACHE_FOLDER_NAME = ".cache";

    public static String ENCODING = "UTF-8";
}
