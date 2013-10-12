package rs.pedjaapps.trakttvandroid.model;

import android.util.Log;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import rs.pedjaapps.trakttvandroid.utils.Constants;

public class PostParams
{
    public enum POST_PARAMS
    {
        USERNAME_FIELD("username"),
        PASSWORD_FIELD("password");
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
	
	private ArrayList<NameValuePair> postParameters;

    public PostParams()
    {
        postParameters = new ArrayList<NameValuePair>();
    }

    public ArrayList<NameValuePair> getPostParameters()
    {
        return postParameters;
    }
	
	public String getJsonStringFromParams()
	{
		JSONObject json = new JSONObject();
		for(NameValuePair pair : postParameters)
		{
			try
			{
				json.put(pair.getName(), pair.getValue());
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return json.toString();
	}

    public void setParamsForAuth(String username, String password)
    {
        postParameters.add(new BasicNameValuePair(POST_PARAMS.PASSWORD_FIELD.value(), password.trim()));
        postParameters.add(new BasicNameValuePair(POST_PARAMS.USERNAME_FIELD.value(), username));
    }

    public void logParams()
    {
        if (postParameters != null)
        {
            for (NameValuePair nameValuePair : postParameters)
            {
                Log.v(Constants.LOG_TAG, "postParameters: [" + nameValuePair.getName() + "] " + "[" + nameValuePair.getValue() + "]");
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
}
