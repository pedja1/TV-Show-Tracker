package rs.pedjaapps.tvshowtracker.utils;

import android.util.Log;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import rs.pedjaapps.tvshowtracker.BuildConfig;

public class PostParams
{

    public enum POST_PARAMS
    {
        TOKEN("token");
        private final String mValue;

        POST_PARAMS(String value)
        {
            mValue = value;
        }

        public String value()
        {
            return mValue;
        }
    }
    private final ArrayList<NameValuePair> postParameters;

    public PostParams()
    {
        postParameters = new ArrayList<NameValuePair>();
    }

    public ArrayList<NameValuePair> getPostParameters()
    {
        return postParameters;
    }

    public void setParamsForToken(String token)
    {
        postParameters.add(new BasicNameValuePair(POST_PARAMS.TOKEN.value(), token));
    }

    public void logParams()
    {
        if (postParameters != null)
        {
            for (NameValuePair nameValuePair : postParameters)
            {
                if(BuildConfig.DEBUG)Log.v(Constants.LOG_TAG, "postParameters: [" + nameValuePair.getName() + "] " + "[" + nameValuePair.getValue() + "]");
            }
        }
    }

    public String getValueByName(String name)
    {
        String result = "";
        for (NameValuePair nameValuePair : postParameters)
        {
            if (nameValuePair.getName().equalsIgnoreCase(name))
            {
                result = nameValuePair.getValue();
            }
        }
        return result;
    }
    
    public enum LOGIN_TYPE
    {
        LOGIN, FACEBOOK_LOGIN
    }

}
