package rs.pedjaapps.tvshowtracker.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;

public class Utility
{
	
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

    public static int watchedPercent(List<Episode> episodes)
    {
        int watched = 0;
        for (Episode e : episodes)
        {
            if (e.getWatched())
            {
                watched++;
            }
        }
        return (int) ((double) watched / (double) episodes.size() * 100.0);
    }

    public static Episode calculateUpcomingEpisodes(List<Show> shows)
    {
        MyTimer timer = new MyTimer();
        long nowSeconds = System.currentTimeMillis() / 1000;//current time in seconds
        List<Episode> upcomingEpisodes = new ArrayList<Episode>();
        Episode nextEpisode = null;
        for (Show s : shows)
        {
            long hours = Long.MAX_VALUE;
            String status = s.getStatus();
            List<Episode> episodes = s.getEpisodes();
            if (status != null && !status.equals("Ended") && episodes != null && !episodes.isEmpty())
            {
                for(Episode e : episodes)
                {
                    if(e.getFirst_aired() != null && e.getFirst_aired() > 0)
                    {
                        long tmp = (e.getFirst_aired() - nowSeconds) / 3600;//convert seconds to hours
                        if(tmp <= 0)tmp = Long.MAX_VALUE;//if hour is negative means we are already past that time
                        e.setAirsIn(tmp);
                    }
                    else
                    {
                        e.setAirsIn(Long.MAX_VALUE);
                    }
                }
                Collections.sort(episodes, new Comparators.EpisodeHourComparator());
                Episode e = episodes.get(0);
                if(e.getAirsIn() != Long.MAX_VALUE)
                {
                    e.setShowTitle(s.getTitle());
                    upcomingEpisodes.add(e);
                    hours = e.getAirsIn();
					s.setUpcomingEpisode(e);
                }
            }
            s.setNextEpisodeHours(hours);
            //s.setWatchedPercent(watchedPercent(s.getEpisodes()));
        }
        Collections.sort(upcomingEpisodes, new Comparators.EpisodeHourComparator());
        if(!upcomingEpisodes.isEmpty())
        {
            nextEpisode = upcomingEpisodes.get(0);
        }
        timer.log("");
        return nextEpisode;
    }


    public static String generateUpcomingEpisodeText(Episode upcomingEpisode, boolean includeShowName)
    {
        return upcomingEpisode.getTitle() 
		+ (includeShowName ? (" (" + upcomingEpisode.getShowTitle() + " " + "S"
        + upcomingEpisode.getSeason() + "E" + upcomingEpisode.getEpisode() + " )") : "")
		+ " - " + MainApp.getContext().getString(R.string.airs) + " "
        + generateEpisodeAirsTime(upcomingEpisode);
    }

    public static String generateEpisodeAirsTime(Episode episode)
    {
        long airTime = episode.getAirsIn();
        //long timeSecondsNow = new Date().getTime() / 1000;
        int hours = (int) airTime;
        int days = hours / 24;

        if(hours <= 1)
        {
            return MainApp.getContext().getString(R.string.less_than_an_hour);
        }
        else if(hours < 24)
        {
            return MainApp.getContext().getString(R.string.hours, hours);
        }
        else if(hours < 48)
        {
            return MainApp.getContext().getString(R.string.tomorow);
        }
        else if(days < 365)
        {
            return MainApp.getContext().getString(R.string.days, days);
        }
        else
        {
            return MainApp.getContext().getString(R.string.more_than_a_year);
        }
    }

    public static String generateEpisodeAiredTime(long airTime)
    {
        if(airTime == 0)return null;
        int hours = (int) ((airTime - (new Date().getTime() / 1000)) / 3600);
        int days = hours / 24;

        if (hours > 0)
        {
            String prefix = MainApp.getContext().getString(R.string.airs);
            if(hours <= 1)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.less_than_an_hour);
            }
            else if(hours < 24)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.hours, hours);
            }
            else if(hours < 48)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.tomorow);
            }
            else if(days < 365)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.days, days);
            }
            else
            {
                return prefix + " " + MainApp.getContext().getString(R.string.more_than_a_year);
            }
        }
        else
        {
            String prefix = MainApp.getContext().getString(R.string.aired);
            if(hours >= -1)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.less_than_an_hour_ago);
            }
            else if(hours > -24)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.hours_ago, Math.abs(hours));
            }
            else if(hours > -48)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.yesterday);
            }
            else if(days > -365)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.days_ago, Math.abs(days));
            }
            else
            {
                return prefix + " " + MainApp.getContext().getString(R.string.more_than_a_year_ago);
            }
        }
    }


    public static boolean hasJB()
    {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN;
    }

    public static void showDeleteDialog(Context context, Show show, DialogInterface.OnClickListener posClickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.delete_confirm, show.getTitle()));
        builder.setPositiveButton(android.R.string.yes, posClickListener);
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    /**
     * Check if email is in valid format(eg. test@gmail.com)
     * */
    public static boolean isEmailValid(String email)
    {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Generate actual path of the {@link android.net.Uri} by looking in the MediaStore
     * @param uri uri to resolve
     * @param activity context
     * @return actual path or null if media isn't in database or other error occurs
     * */
    public static String getActualPathFromUri(Uri uri, Activity activity)
    {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null)
        {
            int column_index = cursor.getColumnIndex(projection[0]);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else
        {
            return null;
        }
    }
	
	public static void deleteShowFromDb(Show show)
	{
		ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
		EpisodeDao eDao = MainApp.getInstance().getDaoSession().getEpisodeDao();
		ActorDao aDao = MainApp.getInstance().getDaoSession().getActorDao();
		GenreDao gDao = MainApp.getInstance().getDaoSession().getGenreDao();
		ImageDao iDao = MainApp.getInstance().getDaoSession().getImageDao();
		if(show != null)
		{
			eDao.deleteInTx(show.getEpisodes());
			aDao.deleteInTx(show.getActors());
			gDao.deleteInTx(show.getGenres());
			iDao.delete(show.getImage());
			showDao.delete(show);
		}
	}

    public enum ImageSize
    {
        /**
         * uncompressed image (poster = 1000x1500, fanart = 1920x1080, some of 1280x720, episode = 400x225,
         * banner = 758x140)*/
        UNCOMPRESSED,

        /**
         * Small posters are 138x203, all the grid views use this size.*/
        SMALL_POSTER,

        /**
         * Large posters are 300x450, the movie summary page uses this size.*/
        LARGE_POSTER,

        /**
         * Large fanart is 940x529, the show summary uses these.*/
        SMALL_FANART,

        /**
         * Small fanart is 218x123, these are used in place of missing episode images.*/
        LARGE_FANART,

        /**
         * Small episodes are 218x123, these are used in things like charts and season pages.*/
        SMALL_EPISODE
    }

    public static String generatePosterUrl(ImageSize imageSize, String url)
    {
        if(TextUtils.isEmpty(url))return url;
        String append = "";
        switch (imageSize)
        {
            case SMALL_POSTER:
                append = "-138";
                break;
            case LARGE_POSTER:
                append = "-300";
                break;
            case UNCOMPRESSED:
                break;
            default:
                Log.w(Constants.LOG_TAG, "Unsupported image size for poster, using uncompressed");
                break;
        }
        String urlNoExt = url.substring(0, url.lastIndexOf("."));
        String extension = url.substring(url.lastIndexOf("."), url.length());
        return urlNoExt + append + extension;
    }
}
