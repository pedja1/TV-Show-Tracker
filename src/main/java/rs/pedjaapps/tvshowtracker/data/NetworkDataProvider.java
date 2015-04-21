package rs.pedjaapps.tvshowtracker.data;

import android.app.*;
import android.os.*;
import android.util.*;
import rs.pedjaapps.tvshowtracker.*;
import rs.pedjaapps.tvshowtracker.network.*;
import rs.pedjaapps.tvshowtracker.utils.*;


/**
 * Created by pedja on 7.11.14. 09.34.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class NetworkDataProvider<T> implements DataProvider<T>
{
    RequestBuilder builder;
    T resultData;
    int requestCode;
    Handler handler;
    Object[] optParams;
    Activity activity;

    public NetworkDataProvider(RequestBuilder builder, int requestCode, Object... optParams)
    {
        this(null, builder, requestCode, optParams);
    }

    public NetworkDataProvider(Activity activity, RequestBuilder builder, int requestCode, Object... optParams)
    {
        if(builder == null)
        {
            throw new IllegalArgumentException("RequestBuilder cannot be null");
        }
        if(requestCode <= 0)
        {
            throw new IllegalArgumentException("invalid request code");
        }
        this.activity = activity;
        this.optParams = optParams;
        this.builder = builder;
        this.requestCode = requestCode;
        handler = new Handler(Looper.getMainLooper());//main thread handler for displaying toasts
    }

    @Override
    public boolean load()
    {
        if(!Network.isNetworkAvailable(MainApp.getContext()))
        {
            return false;
        }
        else
        {
            if(PrefsManager.DEBUG()) Log.d(Constants.LOG_TAG, String.format("NetworkDataProvider::load()[requestCode=%s]", requestCode));
            final JSONUtility jsonUtility = new JSONUtility(Internet.executeHttpRequest(builder));
            switch (requestCode)
            {
                case REQUEST_CODE_SOMETHING:
                    //jsonUtility.parseCompanyDetails();
                    break;
            }
            if (jsonUtility.isSuccess())
            {
                if (jsonUtility.checkResponse())
                {
                    resultData = jsonUtility.getParseObject();
                    return true;
                }
                else
                {
                    if(jsonUtility.getResponse() != null && jsonUtility.getResponse().message != null)
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Utility.showToast(MainApp.getContext(), jsonUtility.getResponse().message);
                            }
                        });
                    return false;
                }
            }
            else
            {
                final Internet.Response response = jsonUtility.getServerResponse();
                if(response != null)
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Utility.showToast(MainApp.getContext(),
                                    response.responseMessage != null && !response.isResponseOk() ? response.responseMessage : MainApp.getContext().getString(R.string.unknown_error));

                        }
                    });
                }
                return false;
            }
        }
    }

    @Override
    public T getResult()
    {
        return resultData;
    }
}
