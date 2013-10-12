package rs.pedjaapps.trakttvandroid.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import rs.pedjaapps.trakttvandroid.model.PostParams;

public class Internet
{
    private static Internet internet = null;
    private DefaultHttpClient httpClient;

    public Internet()
    {
        httpClient = getThreadSafeClient();
        HttpParams httpParameters = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, Constants.CONN_TIMEOUT);
        httpClient.setParams(httpParameters);
    }

    public static DefaultHttpClient getThreadSafeClient()
    {

        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
        return client;
    }

    public static synchronized Internet getInstance()
    {
        if (internet == null)
        {
            internet = new Internet();
        }
        return internet;
    }

	public String httpPost(String url, PostParams params)
    {
        String dataFromUrl = null;
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params.getPostParameters()));

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

	public String httpPost(String url, String json)
    {
        String dataFromUrl = null;
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			
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
	
	
    public String httpGet(String url)
    {
        String dataFromUrl = null;

        try
        {
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

    public HttpResponse httpGetResponse(String url)
    {
        HttpResponse response = null;

        try
        {
            HttpGet httpGet = new HttpGet(url);

            response = httpClient.execute(httpGet);
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

        return response;
    }
}
