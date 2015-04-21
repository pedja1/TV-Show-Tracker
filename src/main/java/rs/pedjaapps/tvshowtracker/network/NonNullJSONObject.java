package rs.pedjaapps.tvshowtracker.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pedja on 12.1.15. 09.27.
 * This class is part of the SocNet
 * Copyright © 2015 ${OWNER}
 */
public class NonNullJSONObject
{
    private final JSONObject jsonObject;

    public NonNullJSONObject(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public NonNullJSONObject(String json) throws JSONException
    {
        jsonObject = new JSONObject(json);
    }

    public String optString(String name)
    {
        return optString(name, null);
    }

    /**
     * never return string representation of null eg "null" or empty string*/
    public String optString(String name, String fallback)
    {
        if(jsonObject.isNull(name))return null;
        String res = jsonObject.optString(name, fallback);
        return res != null && !res.isEmpty() ? res : null;
    }

    public int optInt(String name)
    {
        return jsonObject.optInt(name);
    }

    public int optInt(String name, int fallback)
    {
        return jsonObject.optInt(name, fallback);
    }

    public JSONArray optJSONArray(String name)
    {
        return jsonObject.optJSONArray(name);
    }

    public JSONObject optJSONObject(String name)
    {
        return jsonObject.optJSONObject(name);
    }

    public long optLong(String name, long fallback)
    {
        return jsonObject.optLong(name, fallback);
    }

    public long optLong(String name)
    {
        return jsonObject.optLong(name);
    }

    public double optDouble(String name)
    {
        return jsonObject.optDouble(name);
    }

    public double optDouble(String name, double fallback)
    {
        return jsonObject.optDouble(name, fallback);
    }

    public boolean optBoolean(String name)
    {
        return jsonObject.optBoolean(name);
    }

    public boolean optBoolean(String name, boolean fallback)
    {
        return jsonObject.optBoolean(name, fallback);
    }
}
