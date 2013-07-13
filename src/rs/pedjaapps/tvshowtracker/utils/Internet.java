package rs.pedjaapps.tvshowtracker.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created by pedja on 7/13/13.
 */
public class Internet
{
    public static String httpPost(String url)
    {
        String dataFromUrl = null;

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            dataFromUrl = EntityUtils.toString(httpEntity, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
        }
        catch (MalformedURLException e)
        {
        }
        catch (IOException e)
        {
        }

        return dataFromUrl;
    }

    public static String httpGet(String url)
    {
        String dataFromUrl = null;

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            dataFromUrl = EntityUtils.toString(httpEntity);
        }
        catch (UnsupportedEncodingException e)
        {
        }
        catch (MalformedURLException e)
        {
        }
        catch (IOException e)
        {
        }

        return dataFromUrl;
    }
}
