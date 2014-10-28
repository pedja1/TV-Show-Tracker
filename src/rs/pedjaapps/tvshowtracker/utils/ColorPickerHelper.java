package rs.pedjaapps.tvshowtracker.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import java.util.HashSet;
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
    private HashSet<Integer> processing;

    public Handler uiHandler;

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
        uiHandler = new Handler();
        queue = new LinkedBlockingQueue<Holder>();
        processing = new HashSet<>();
        //create threads up to the pool size
        workerThreads = new ColorPickerThread[DEFAULT_CACHE_THREAD_POOL_SIZE];
        //start processing on all threads
        for(ColorPickerThread userCacheThread : workerThreads)
        {
            userCacheThread = new ColorPickerThread(queue);
            userCacheThread.start();
        }
    }

    public void setColorFromBitmap(Bitmap bitmap, Show show, View view)
    {
        Holder holder = new Holder(bitmap, show, view);
        if(show.getPosterMainColor() != -1)
        {
            view.setBackgroundColor(show.getPosterMainColor());
            return;
        }
        if(isQueuedOrProcessing(holder))
        {
            return;
        }
        queue.add(holder);
    }

    private boolean isQueuedOrProcessing(Holder holder)
    {
        return queue.contains(holder) || processing.contains(holder.show.getTvdb_id());
    }

    void startProcessing(int tvdbId)
    {
        processing.add(tvdbId);
    }

    void finishProcessing(int tvdbId)
    {
        processing.remove(tvdbId);
    }

    public static class Holder
    {
        Bitmap bitmap;
        Show show;
        View view;

        public Holder(Bitmap bitmap, Show show, View view)
        {
            this.bitmap = bitmap;
            this.show = show;
            this.view = view;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Holder holder = (Holder) o;

            return show.getTvdb_id() == holder.show.getTvdb_id();

        }

        @Override
        public int hashCode()
        {
            return show.getTvdb_id();
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
