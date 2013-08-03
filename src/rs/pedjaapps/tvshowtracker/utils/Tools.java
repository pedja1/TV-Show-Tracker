package rs.pedjaapps.tvshowtracker.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

public class Tools {
	
	private static boolean isRefresh = true;
	
	public static boolean isRefresh()
	{
		return isRefresh;
	}

	public static void setRefresh(boolean refresh)
	{
		isRefresh = refresh;
	}
	
	public static void setKeepScreenOn(Activity activity, boolean keepScreenOn) {
	    if(keepScreenOn) {
	      activity.getWindow().
	        addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    } else {
	      activity.getWindow().
	        clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    }
	  }


	public static String DownloadFromUrl(String imageURL, String fileName, boolean useCached) {  //this is the downloader method
        try {
        	File mdbDir = new File(Environment.getExternalStorageDirectory() + "/MDb/posters");
		      if(!mdbDir.exists()){
		      mdbDir.mkdirs();
		      }
                URL url = new URL(imageURL);
                File file = new File(fileName);
                if(useCached){
                if(file.exists()){
                	return fileName;
                }
                }
                
               /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current;
                while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                }

                /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
			return fileName;

        } catch (Exception e) {
                Log.e("error saving image", e.getMessage());
        	return "";
		}

	}
	
	public static double parseRating(String rating){
		try{
			return Double.parseDouble(rating);
		}
		catch(Exception e)
		{
			return 0.0;
		}
	}
	public static int parseInt(String runtime){
		try{
			return Integer.parseInt(runtime);
		}
		catch(Exception e){
			return 0;
		}
	}
	
	public static boolean isNetworkAvailable(Context context) 
	{
	    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}
	
}
