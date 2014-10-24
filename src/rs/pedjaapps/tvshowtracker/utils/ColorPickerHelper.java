package rs.pedjaapps.tvshowtracker.utils;

import android.graphics.Bitmap;

import java.util.concurrent.LinkedBlockingQueue;

import rs.pedjaapps.tvshowtracker.model.Show;

/**
 * Created by pedja on 24.10.14. 16.54.
 * This class is part of the tvst
 * Copyright © 2014 ${OWNER}
 */
public class ColorPickerHelper
{
    private static ColorPickerHelper instance;

    private static final int DEFAULT_CACHE_THREAD_POOL_SIZE = 4;

    private LinkedBlockingQueue<Holder> queue;
    private ColorPickerThread[] workerThreads;

    public static ColorPickerHelper getInstance()
    {
        if(instance == null)
        {
            instance = new ColorPickerHelper();
        }
        return instance;
    }

    public ColorPickerHelper()
    {
        queue = new LinkedBlockingQueue<Holder>();
        //create threads up to the pool size
        workerThreads = new ColorPickerThread[DEFAULT_CACHE_THREAD_POOL_SIZE];
        //start processing on all threads
        for(ColorPickerThread userCacheThread : workerThreads)
        {
            userCacheThread = new ColorPickerThread(queue);
            userCacheThread.start();
        }
    }

    public void addToQueue(Bitmap bitmap, Show show)
    {
        if(!queue.contains(bitmap))
        {
            queue.add(bitmap);
        }
    }

    public boolean isQueued(Bitmap bitmap)
    {
        return queue.contains(bitmap);
    }

    public void processingFinished()
    {

    }

    public static class Holder
    {
        Bitmap bitmap;
        Show show;

        public Holder(Bitmap bitmap, Show show)
        {
            this.bitmap = bitmap;
            this.show = show;
        }
    }

    /**
     * Clear instance, clear queue and list of cached users, shutdown all worker threads
     * */
    public void cleanup()
    {
        instance = null;
        queue.clear();
        for (ColorPickerThread thread : workerThreads) thread.quit();
    }
}
