package rs.pedjaapps.tvshowtracker.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constants {
	public static final String apiKey = "E4BD239A1D7130F7";
	public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
	public static final int SHOW_DOWNLOAD_CODE = 000;
	public static final int SHOW_UPDATE_CODE = 111;
	public static final int UI_CODE_PRELOAD = 0;
	public static final int UI_CODE_AFTERLOAD = 1;
	public static final int REFRESH_HANDLER_UPDATE_LIST = 1001;
	public static final String LOG_TAG = "tvst";
    public static final String ENCODING = "UTF-8";
    public static final String URL_ROOT = "http://thetvdb.com";
    public static final int CONN_TIMEOUT = 20 * 1000;

    // **************************************************
// ImageManager
// **************************************************
    public static final String CACHE_FOLDER = "cache";
    public static final String PATH_TO_MEDIA = Environment.getExternalStorageDirectory().getPath();
    public static final String EXTERNAL_FOLDER_ROOT = "data/tvst";
    public static final String EXTERNAL_FOLDER_DATA = Constants.PATH_TO_MEDIA + File.separator + Constants.EXTERNAL_FOLDER_ROOT;
    public static final String CACHE_FULL_PATH = Environment.getExternalStorageDirectory()
            + File.separator + EXTERNAL_FOLDER_ROOT
            + File.separator + CACHE_FOLDER;
}
