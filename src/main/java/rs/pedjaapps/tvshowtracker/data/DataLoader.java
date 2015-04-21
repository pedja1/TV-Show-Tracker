package rs.pedjaapps.tvshowtracker.data;

import java.util.*;
import rs.pedjaapps.tvshowtracker.utils.*;

/**
 * Created by pedja on 6.11.14. 15.39.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class DataLoader<T>
{
    public interface LoadListener<T>
    {
        public void onLoadingFinished(Result<T> result);
        public void onLoadStarted();
    }

    /**
     * This class holds load result
     * */
    public static class Result<T>
    {
        public static final int STATUS_OK = 1001;
        public static final int STATUS_ERROR = 1001;
        public final int status;
        public final T data;

        public Result(T data, int status)
        {
            this.data = data;
            this.status = status;
        }
    }

    private LoadListener<T> listener;
    private List<DataProvider<T>> providers;
    private ATLoader atLoader;

    /**
     * @param listener optional {@link com.preezm.preezm.data.DataLoader.LoadListener}
     * @param providers List of {@link DataProvider}s.<br>
     *                  Providers will be used in order they are placed in the list.<br>
     *                 */
    public DataLoader( LoadListener<T> listener,  List<DataProvider<T>> providers)
    {
        this.listener = listener;
        this.providers = providers;
    }

    public DataLoader(List<DataProvider<T>> providers)
    {
        this(null, providers);
    }

    public DataLoader()
    {
        this(null, null);
    }

    public void setListener(LoadListener<T> listener)
    {
        this.listener = listener;
    }

    /**
     * List of providers for this loader<br>
     * Providers are used in order they are placed in a list, make sure that you place providers with higher priority first<br><br>*/
    public void setProviders(List<DataProvider<T>> providers)
    {
        this.providers = providers;
    }

    /**
     * Start loading data<br><br>
     * This method will throw an exception if provider list is null or empty<br><br>
     * Loading is done on a worker thread, unless 'forceSerial' is true<br><br>
     * {@link DataProvider#load()} method will be called for
     * each provider until one of them returns true<br><br>
     * Providers are used in order they are placed in a list, make sure that you place providers with higher priority first<br><br>
     * Result will be delivered in {@link com.preezm.preezm.data.DataLoader.LoadListener#onLoadingFinished(com.preezm.preezm.data.DataLoader.Result)}
     * @param forceSerial force serial execution. Data loading will be executed on a caller thread
     * @return if forceSerial is true returns result, if its false returns loading result*/
    public Result<T> loadData(boolean forceSerial)
    {
        if(providers == null || providers.isEmpty())
        {
            throw new IllegalStateException("DataProvider array cannot be null or empty");
        }
        if (!forceSerial)
        {
            atLoader = new ATLoader();
            atLoader.execute();
        }
        else
        {
            return _loadData();
        }
        return null;
    }

    public void loadData()
    {
        loadData(false);
    }

    public Result<T> _loadData()
    {
        for(DataProvider<T> provider : providers)
        {
            boolean success = provider.load();
            if(success)
            {
                T result = provider.getResult();
                return new Result<>(result, Result.STATUS_OK);
            }
        }
        return new Result<>(null, Result.STATUS_ERROR);
    }

    private class ATLoader extends AsyncTask<String, Void, Result<T>>
    {
        @Override
        public Result<T> doInBackground(String... params)
        {
            return _loadData();
        }

        @Override
        public void onPostExecute(Result<T> result)
        {
            if(listener != null)listener.onLoadingFinished(result);
        }

        @Override
        public void onPreExecute()
        {
            if(listener != null)listener.onLoadStarted();
        }
    }

    /**
     * Cancel loading
     * This only calls cancel(true) on the underlying AsyncTask*/
    public void cancel()
    {
        if(atLoader != null)atLoader.cancel(true);
    }
}
