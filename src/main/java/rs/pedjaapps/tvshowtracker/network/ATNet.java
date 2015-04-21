package rs.pedjaapps.tvshowtracker.network;

import android.app.*;
import android.content.*;
import android.util.*;
import rs.pedjaapps.tvshowtracker.*;
import rs.pedjaapps.tvshowtracker.utils.*;

/**
 * Created by pedja on 1/22/14 10.11.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 * @author Predrag Čokulov
 */
public class ATNet<Params ,Progress, Result> extends AsyncTask<Params, Progress, Result>
{
	public static final SparseArray<AsyncTask> activeTasks = new SparseArray<>();
    private AlertDialog networkDialog;
	private ATListener<Params, Progress, Result> atListener;
    private final int requestCode;
    private Activity activity;

    public ATNet(ATListener<Params, Progress, Result> atListener, int requestCode, Activity activity)
    {
        this.atListener = atListener;
        this.requestCode = requestCode;
        this.activity = activity;
        if (activity == null)
        {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (atListener == null)
        {
            throw new IllegalArgumentException("ATListener must not be null");
        }
    }

    @SuppressWarnings("unchecked")
    public ATNet(int requestCode, Activity activity)
    {
        if (!(activity instanceof ATListener))
        {
            throw new IllegalArgumentException("Your activity must implement ATListener");
        }
        this.atListener = (ATListener<Params, Progress, Result>) activity;
        this.requestCode = requestCode;
        this.activity = activity;
    }

    /**
     * Calls {@link #atListener#doInBackground(int, Object[])}
     * */
    @Override
    protected Result doInBackground(Params... params)
    {
        return atListener.doInBackground(requestCode, params);
    }

    /**
     * Calls {@link #atListener#onPostExecute(Object)}
     * */
    @Override
    protected void onPostExecute(Result result)
    {
        if (atListener != null) atListener.onPostExecute(requestCode, result);
        activeTasks.remove(requestCode);
        activity = null;
    }

    /**
     * Calls {@link #atListener#onProgressUpdate(Object[])}
     * */
    @Override
    protected void onProgressUpdate(Progress... values)
    {
        if (atListener != null) atListener.onProgressUpdate(requestCode, values);
    }

    /**
     * Calls {@link #atListener#onPreExecute()}
     * */
    @Override
    protected void onPreExecute()
    {
        if (!Network.isNetworkAvailable(activity))//if we don't have internet connection prevent task from starting and notify user
        {
            showNoInternetDialog(activity);
            cancel(true);
            return;
        }
        if (atListener != null) atListener.onPreExecute(requestCode);
    }

    /**
     * Shows dialog indicating that internet connection is not available*/
    private void showNoInternetDialog(final Activity activity)
    {
        if (networkDialog != null && networkDialog.isShowing())
		{
			return;
		}
		networkDialog = Utility.buildNoNetworkDialog(activity, R.string.no_connection_dialog_message, R.string.no_connection_dialog_title, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //shutdown app
                    activity.finish();//todo finish?
                    //activity.startActivity(new Intent(activity, ShutdownActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

        if (activity != null && !activity.isFinishing())
        {
            networkDialog.show();
        }
    }

    @Override
    protected void onCancelled(Result result)
    {
        super.onCancelled(result);
        activeTasks.remove(requestCode);
        atListener.onCancelled(requestCode, result);
        activity = null;
    }

    @Override
    public AsyncTask<Params, Progress, Result> execute(Params... params)
    {
        AsyncTask task = activeTasks.get(requestCode);
        if (task != null)
        {
            task.cancel(true);
        }
        activeTasks.put(requestCode, this);
        return super.execute(params);
    }

    public int getRequestCode()
    {
        return requestCode;
    }
}
