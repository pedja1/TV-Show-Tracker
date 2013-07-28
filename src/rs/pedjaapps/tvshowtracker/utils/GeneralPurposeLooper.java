package rs.pedjaapps.tvshowtracker.utils;

import android.os.Handler;
import android.os.Looper;

public class GeneralPurposeLooper extends Thread
{
    private static GeneralPurposeLooper gpLooper = null;
    private Handler handler = null;
    private static final int priority = Thread.NORM_PRIORITY - 1;
    private static final String name = "GeneralPurposeThread";

    @Override
    public void run()
    {
        Looper.prepare();
        synchronized (this)
        {
            handler = new Handler();
            // notify that we are in business
            notify();
        }
        Looper.loop();
    }

    // get a reference or instantiate our looper
    public static synchronized GeneralPurposeLooper getInstance()
    {
        if (gpLooper == null)
        {
            gpLooper = new GeneralPurposeLooper();
            gpLooper.setName(name);
            gpLooper.setPriority(priority);
            if (gpLooper.getState() == Thread.State.NEW)
            {
                gpLooper.start();
            }
        }
        return gpLooper;
    }

    // just getting the handler, ignore the code
    public static Handler getHandler()
    {
        GeneralPurposeLooper gpl = getInstance();
        Handler h;
        synchronized (gpl)
        {
            h = gpl.handler;
            while (h == null)
            {
                try
                {
                    // wait for the looper to instantiate
                    gpl.wait();
                }
                catch (InterruptedException ex)
                {
                }
                h = gpl.handler;
            }
        }
        return h;
    }

    // give looper a task
    public boolean post(Runnable runnable)
    {
        return getHandler().post(runnable);
    }

    /* kill the looper by queuing the quit task
     * we can start another by calling getInstance
     */
    public synchronized void requestStop()
    {
        getHandler().post(new Runnable()
        {
            public void run()
            {
                Looper.myLooper().quit();
            }
        });
    }
}
