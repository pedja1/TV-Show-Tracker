package rs.pedjaapps.tvshowtracker.network;

/**
 * Created by pedja on 1/22/14 10.11.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 * @author Predrag Čokulov
 */
public interface ATListener<Params, Progress, Result>
{
    /**
     * @param requestCode for this task
     * @see com.preezm.preezm.utility.AsyncTask#doInBackground(Object[])
     * */
    public Result doInBackground(int requestCode, Params... params);

    /**
     * @param requestCode for this task
     * @see com.preezm.preezm.utility.AsyncTask#onPostExecute(Object)
     * */
    public void onPostExecute(int requestCode, Result result);

    /**
     * @param requestCode for this task
     * @see com.preezm.preezm.utility.AsyncTask#onPreExecute()
     * */
    public void onPreExecute(int requestCode);

    /**
     * @param requestCode for this task
     * @see com.preezm.preezm.utility.AsyncTask#onProgressUpdate(Object[])
     * */
    public void onProgressUpdate(int requestCode, Progress... values);

    /**
     * @param requestCode for this task
     * @see com.preezm.preezm.utility.AsyncTask#onCancelled(Object)
     * */
    public void onCancelled(int requestCode, Result result);
}
