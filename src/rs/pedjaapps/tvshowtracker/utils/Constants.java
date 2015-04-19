package rs.pedjaapps.tvshowtracker.utils;

import java.text.SimpleDateFormat;

public class Constants
{
    public static final String CLIENT_ID = "21d99c083e81a2906244853ecec854b15ae2a50cc9387ce337f26165f1f10995";
    public static final String CLIENT_SECRET = "d5715cdcc16836378409d7c42f9821a19051fccc41d80bcd49cd81cbd7a5f7c5";
    public static final String TRAKT_API_KEY = "f18fce5c75ed1f25aef95c3626ba8935";
    public static final String REQUEST_URL_SEARCH = "http://api.trakt.tv/search/shows.json/" + TRAKT_API_KEY;
	public static final String REQUEST_URL_TRENDING = "http://api.trakt.tv/shows/trending.json/" + TRAKT_API_KEY;
    public static final String REQUEST_URL_GET_SHOW_INFO = "http://api.trakt.tv/show/summary.json/" + TRAKT_API_KEY;
    public static final String IMDB_URL_PREFIX = "http://www.imdb.com/title/";
    public static final String REQUEST_URL_LOGIN = "http://api.trakt.tv/account/settings/" + TRAKT_API_KEY;
    public static final String REQUEST_URL_COLLECTION = "http://api.trakt.tv/user/library/shows/collection.json/" + TRAKT_API_KEY + "/";
    public static final String REQUEST_URL_ADD_TO_LIBRARY = "http://api.trakt.tv/show/episode/library/" + TRAKT_API_KEY;

    public static final String apiKey = "E4BD239A1D7130F7";//tvdb key, not used anymore
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
    public static final String LOG_TAG = "tvst";
    public static final String DB_TABLE_NAME = "tvst.db";
    public static final int CONN_TIMEOUT = 2 * 60 * 1000;//2 min
    public static final String TRAKT_URL = "http://trakt.tv";
    public static final String CACHE_FOLDER_NAME = ".cache";

    public static String ENCODING = "UTF-8";
}
