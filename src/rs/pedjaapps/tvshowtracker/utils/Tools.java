package rs.pedjaapps.tvshowtracker.utils;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.text.Html;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import org.apache.http.util.*;

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
	
	public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null)
        {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++)
            {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            System.out.println(totalHeight);
        }
    }

    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, String message,
                                                     String title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title != null ? Html.fromHtml(title) : null);
        builder.setMessage(message != null ? Html.fromHtml(message) : null);
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, int message,
                                                     int title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Html.fromHtml(context.getString(title)));
        builder.setMessage(Html.fromHtml(context.getString(message)));
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * No Network dialog
     */
    public static AlertDialog buildNoNetworkDialog(Context context, int message,
                                                   int title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, listener);
        return builder.create();
    }

    /**
     * General Purpose Toast
     */
    public static void showToast(Context context, String message)
    {
        Toast.makeText(context, message != null ? Html.fromHtml(message) : null, Toast.LENGTH_LONG).show();
    }

    /**
     * General Purpose Toast
     */
    public static void showToast(Context context, int resId)
    {
        Toast.makeText(context, Html.fromHtml(context.getString(resId)), Toast.LENGTH_LONG).show();
    }
	
}
