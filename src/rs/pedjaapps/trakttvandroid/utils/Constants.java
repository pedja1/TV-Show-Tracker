package rs.pedjaapps.trakttvandroid.utils;

import java.text.SimpleDateFormat;

public class Constants {
	public static final String apiKey = "E4BD239A1D7130F7";
	public static final String traktApiKey = "f18fce5c75ed1f25aef95c3626ba8935";
	public static final String URL_ROOT = "http://api.trakt.tv/";
	public static final String URL_LOGIN = URL_ROOT + "account/settings/" + traktApiKey;
	public static final String URL_SEARCH_SHOWS = URL_ROOT + "search/shows.format/" + traktApiKey + "/";
	public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
	public static final int SHOW_DOWNLOAD_CODE = 000;
	public static final int SHOW_UPDATE_CODE = 111;
	public static final int UI_CODE_PRELOAD = 0;
	public static final int UI_CODE_AFTERLOAD = 1;
	public static final int REFRESH_HANDLER_UPDATE_LIST = 1001;
	public static final String LOG_TAG = "tvst";

	public static String ENCODING = "UTF-8";

	public static final int CONN_TIMEOUT = 20000;
}
