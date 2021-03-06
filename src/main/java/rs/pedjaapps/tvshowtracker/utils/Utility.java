package rs.pedjaapps.tvshowtracker.utils;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import org.apache.http.util.*;
import rs.pedjaapps.tvshowtracker.*;
import rs.pedjaapps.tvshowtracker.model.*;

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

    public static void setKeepScreenOn(Activity activity, boolean keepScreenOn)
    {
        if (keepScreenOn)
        {
            activity.getWindow().
                    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else
        {
            activity.getWindow().
                    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    public static String DownloadFromUrl(String imageURL, String fileName, boolean useCached)
    {  //this is the downloader method
        try
        {
            File mdbDir = new File(Environment.getExternalStorageDirectory() + "/MDb/posters");
            if (!mdbDir.exists())
            {
                mdbDir.mkdirs();
            }
            URL url = new URL(imageURL);
            File file = new File(fileName);
            if (useCached)
            {
                if (file.exists())
                {
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
            while ((current = bis.read()) != -1)
            {
                baf.append((byte) current);
            }

                /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            return fileName;

        }
        catch (Exception e)
        {
            Log.e("error saving image", e.getMessage());
            return "";
        }

    }

    public static double parseRating(String rating)
    {
        try
        {
            return Double.parseDouble(rating);
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }

    public static int parseInt(String runtime)
    {
        try
        {
            return Integer.parseInt(runtime);
        }
        catch (Exception e)
        {
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
                for (Episode e : episodes)
                {
                    if (e.getFirst_aired() != null && e.getFirst_aired() > 0)
                    {
                        long tmp = (e.getFirst_aired() - nowSeconds) / 3600;//convert seconds to hours
                        if (tmp <= 0)
                            tmp = Long.MAX_VALUE;//if hour is negative means we are already past that time
                        e.setAirsIn(tmp);
                    }
                    else
                    {
                        e.setAirsIn(Long.MAX_VALUE);
                    }
                }
                Collections.sort(episodes, new Comparators.EpisodeHourComparator());
                Episode e = episodes.get(0);
                if (e.getAirsIn() != Long.MAX_VALUE)
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
        if (!upcomingEpisodes.isEmpty())
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

        if (hours <= 1)
        {
            return MainApp.getContext().getString(R.string.less_than_an_hour);
        }
        else if (hours < 24)
        {
            return MainApp.getContext().getString(R.string.hours, hours);
        }
        else if (hours < 48)
        {
            return MainApp.getContext().getString(R.string.tomorow);
        }
        else if (days < 365)
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
        if (airTime == 0) return null;
        int hours = (int) ((airTime - (new Date().getTime() / 1000)) / 3600);
        int days = hours / 24;

        if (hours > 0)
        {
            String prefix = MainApp.getContext().getString(R.string.airs);
            if (hours <= 1)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.less_than_an_hour);
            }
            else if (hours < 24)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.hours, hours);
            }
            else if (hours < 48)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.tomorow);
            }
            else if (days < 365)
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
            if (hours >= -1)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.less_than_an_hour_ago);
            }
            else if (hours > -24)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.hours_ago, Math.abs(hours));
            }
            else if (hours > -48)
            {
                return prefix + " " + MainApp.getContext().getString(R.string.yesterday);
            }
            else if (days > -365)
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
     */
    public static boolean isEmailValid(String email)
    {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Generate actual path of the {@link android.net.Uri} by looking in the MediaStore
     *
     * @param uri      uri to resolve
     * @param activity context
     * @return actual path or null if media isn't in database or other error occurs
     */
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
	
	/**
     * Encode string as URL UTF-8
     */
    @SuppressWarnings("deprecation")
    public static String encodeString(String string)
    {
        return URLEncoder.encode(string);
    }

}
