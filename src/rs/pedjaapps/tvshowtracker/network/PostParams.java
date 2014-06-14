package rs.pedjaapps.tvshowtracker.network;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class PostParams
{

    public enum Key
    {
        email, password
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

    public void setParamsForLogin(String email, String password)
    {
        postParameters.add(new BasicNameValuePair(Key.email.toString(), email));
        postParameters.add(new BasicNameValuePair(Key.password.toString(), password));
    }

}
