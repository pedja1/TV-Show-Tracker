package rs.pedjaapps.tvshowtracker.network;

import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import ch.boye.httpclientandroidlib.Consts;
import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.config.RequestConfig;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.cookie.Cookie;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;
import ch.boye.httpclientandroidlib.impl.conn.PoolingHttpClientConnectionManager;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import rs.pedjaapps.tvshowtracker.BuildConfig;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.network.cookie.PersistentCookieStore;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.MyTimer;
import rs.pedjaapps.tvshowtracker.utils.PostParams;


public class Internet
{
    private static Internet internet = null;
    private final HttpClient httpClient;
    private final PersistentCookieStore cookieStore;
    private static boolean log = BuildConfig.DEBUG;
    private static boolean printResponse = log && false;

    private Internet()
    {
        cookieStore = new PersistentCookieStore(MainApp.getContext());
        printCookies(cookieStore.getCookies());
        httpClient = getThreadSafeClient();
    }

    private void printCookies(List<Cookie> cookies)
    {
        for(Cookie c : cookies)
        {
            if(BuildConfig.DEBUG)System.out.println("Cookie" + c.getValue());
        }
    }

    /**
     * Create Thread Safe client
     * This client can be freely used across multiple threads and used simultaneously*/
    private HttpClient getThreadSafeClient()
    {
        HttpClientBuilder builder = HttpClients.custom();
        builder.setConnectionManager(new PoolingHttpClientConnectionManager());
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(Constants.CONN_TIMEOUT)
                .setConnectTimeout(Constants.CONN_TIMEOUT)
                .build();
        builder.setDefaultRequestConfig(config);
        builder.setDefaultCookieStore(cookieStore);
        return builder.build();
    }

    public static synchronized Internet getInstance()
    {
        if (internet == null)
        {
            internet = new Internet();
        }
        return internet;
    }

    /**
     * Executes HTTP POST request and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     * @param url server url to make a request to
     * @param params POST parameters
     * @see PostParams
     * @see #httpGet(String)
     * @return server response as string
     * */
    public Response httpPost(String url, PostParams params)
    {
        Response response = new Response();
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params.getPostParameters()));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response.responseData = EntityUtils.toString(httpEntity, Constants.ENCODING);
            response.code = httpResponse.getStatusLine().getStatusCode();
            response.responseMessage = httpResponse.getStatusLine().getReasonPhrase();
            if(log)Log.d(Constants.LOG_TAG, "httpPost[" + url + "]: " + response);
        }
        catch (UnsupportedEncodingException e)
        {
            response.code = Response.RESPONSE_CODE_UNSUPPORTED_ENCODING;
            response.responseMessage = MainApp.getContext().getString(R.string.unsupported_encoding);
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            response.code = Response.RESPONSE_CODE_MALFORMED_URL;
            response.responseMessage = MainApp.getContext().getString(R.string.url_malformed);
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.io_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = url;
        }

        return response;
    }

    /**
     * Executes HTTP POST request and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     * @param url server url to make a request to
     * @param json json string
     * @see #httpGet(String)
     * @see #httpPost(String, PostParams)
     * @return server response as string
     * */
    public Response httpPost(String url, String json)
    {
        Response response = new Response();
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response.responseData = EntityUtils.toString(httpEntity, Constants.ENCODING);
            response.code = httpResponse.getStatusLine().getStatusCode();
            response.responseMessage = httpResponse.getStatusLine().getReasonPhrase();
            if(log)Log.d(Constants.LOG_TAG, "httpPost[" + url + "]: " + response);
        }
        catch (UnsupportedEncodingException e)
        {
            response.code = Response.RESPONSE_CODE_UNSUPPORTED_ENCODING;
            response.responseMessage = MainApp.getContext().getString(R.string.unsupported_encoding);
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            response.code = Response.RESPONSE_CODE_MALFORMED_URL;
            response.responseMessage = MainApp.getContext().getString(R.string.url_malformed);
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.io_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = url;
        }

        return response;
    }

    /**
     * Uploads file to server using HTTP POST and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     * @param url server url to make a request to
     * @param filePath local path of the file to upload
     * @see #httpPost(String, PostParams)
     * @return server response as string
     * */
    public Response httpFilePost(String url, String filePath)
    {
        HttpPost httpPost;
        Response response = new Response();

        try
        {
            httpPost = new HttpPost(url);
            //httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.setCharset(Consts.UTF_8);

            multipartEntityBuilder.addPart("image", new FileBody(new File(filePath)));
            multipartEntityBuilder.addTextBody("gallery_id", "1");
            //multipartEntity.addBinaryBody("image", new File(filePath));

            httpPost.setEntity(multipartEntityBuilder.build());
            if(log)Log.d(Constants.LOG_TAG, "httpFilePost[executing request] " + httpPost.getRequestLine());
            if(log)
            {
                for(Header h : httpPost.getAllHeaders())
                {
                    if(log)Log.d(Constants.LOG_TAG, "httpFilePost[header] " + h.getName() + ":" + h.getValue());
                }
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if(log)Log.d(Constants.LOG_TAG, "httpFilePost[response status] " + httpResponse.getStatusLine().toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            response.responseData = EntityUtils.toString(httpEntity, Constants.ENCODING);
            response.code = httpResponse.getStatusLine().getStatusCode();
            response.responseMessage = httpResponse.getStatusLine().getReasonPhrase();
            if(log)Log.d(Constants.LOG_TAG, "httpFilePost[" + url + "]: " + response);
        }
        catch (UnsupportedEncodingException e)
        {
            response.code = Response.RESPONSE_CODE_UNSUPPORTED_ENCODING;
            response.responseMessage = MainApp.getContext().getString(R.string.unsupported_encoding);
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            response.code = Response.RESPONSE_CODE_MALFORMED_URL;
            response.responseMessage = MainApp.getContext().getString(R.string.url_malformed);
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.io_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = url;
        }

        return response;
    }

    /**
     * Executes HTTP GET request and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     * @param url server url to make a request to
     * @see #httpPost(String, PostParams)
     * @return server response as string
     * */
    public Response httpGet(String url)
    {
        MyTimer timer = new MyTimer();
        Response response = new Response();

        try
        {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            response.responseData = EntityUtils.toString(httpEntity, Constants.ENCODING);
            response.code = httpResponse.getStatusLine().getStatusCode();
            response.responseMessage = httpResponse.getStatusLine().getReasonPhrase();
            if(log)Log.d(Constants.LOG_TAG, "httpGet[" + url + "]: " + response);
        }
        catch (UnsupportedEncodingException e)
        {
            response.code = Response.RESPONSE_CODE_UNSUPPORTED_ENCODING;
            response.responseMessage = MainApp.getContext().getString(R.string.unsupported_encoding);
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            response.code = Response.RESPONSE_CODE_MALFORMED_URL;
            response.responseMessage = MainApp.getContext().getString(R.string.url_malformed);
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.io_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = url;
        }
        timer.log("Internet:httpGet>>time");

        return response;
    }

    /**
     * Executes HTTP GET request and returns HttpResponse object<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     * @param url server url to make a request to
     * @see #httpGet(String)
     * @return HttpResponse object containing response from server
     * */
    public HttpResponse httpGetResponse(String url)
    {
        HttpResponse response = null;

        try
        {
            HttpGet httpGet = new HttpGet(url);
            //httpGet.setHeader("Authorization", "Basic " + Base64.encodeToString((Constants.HTTP_USER + ":" + Constants.HTTP_PASS).getBytes(), Base64.NO_WRAP));

            response = httpClient.execute(httpGet);
        }
        catch (UnsupportedEncodingException e)
        {
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            Crashlytics.logException(e);
        }

        return response;
    }

    public class Response
    {
        public static final int RESPONSE_CODE_UNKNOWN_ERROR = -1;
        public static final int RESPONSE_CODE_UNSUPPORTED_ENCODING = -2;
        public static final int RESPONSE_CODE_MALFORMED_URL = -3;
        public static final int RESPONSE_CODE_IO_ERROR = -4;

        public int code = RESPONSE_CODE_UNKNOWN_ERROR;
        public String responseMessage;
        public String responseData;
        public String request;

        public boolean isResponseOk()
        {
            return code < 400;
        }

        @Override
        public String toString()
        {
            return "Response{" +
                    "code=" + code +
                    ", responseMessage='" + responseMessage + '\'' +
                    (printResponse ? ", responseData='" + responseData : "") + '\'' +
                    '}';
        }
    }

    public PersistentCookieStore getCookieStore()
    {
        return cookieStore;
    }
}