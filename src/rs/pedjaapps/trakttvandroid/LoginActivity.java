package rs.pedjaapps.trakttvandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import rs.pedjaapps.trakttvandroid.model.PostParams;
import rs.pedjaapps.trakttvandroid.utils.Constants;
import rs.pedjaapps.trakttvandroid.utils.Internet;
import rs.pedjaapps.trakttvandroid.R;
import rs.pedjaapps.trakttvandroid.MainActivity;
import rs.pedjaapps.trakttvandroid.model.User;

/**
 * Created by pedja on 9/8/13.
 */
public class LoginActivity extends Activity implements View.OnClickListener
{

	public void onClick(View p1)
	{
		new Login().execute();
	}

    EditText username, password;
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//getActionBar().setSubtitle("Login to trakt.tv");
		username = (EditText)findViewById(R.id.edUsername);
		password = (EditText)findViewById(R.id.edPassword);
		Button login = (Button)findViewById(R.id.btnLogin);
		login.setOnClickListener(this);
    }
	
	private class Login extends AsyncTask<String, Integer, String>
	{
        ProgressDialog pd;
		protected String doInBackground(String[] p1)
		{
	        PostParams params = new PostParams();
			String hash = new String(Hex.encodeHex(DigestUtils.sha1(password.getText().toString().trim())));
			params.setParamsForAuth(username.getText().toString(), hash);
			System.out.println("hash: "+hash + " json: " + params.getJsonStringFromParams());
			String response = Internet.getInstance().httpPost(Constants.URL_LOGIN,  params.getJsonStringFromParams());
			boolean success;
			try
			{
				JSONObject json = new JSONObject(response);
				success = json.getString("status").equals("success");
				if(success)
				{
                                    User user = User.getInstance();
                                    user.setUserFromJson(json);
                                    System.out.print(response);
				}
				else
				{
					return json.getString("error");
				}
			}
			catch (JSONException e)
			{
				return e.getMessage();
			}
			return "0";
		}
		
                @Override
		protected void onPreExecute()
		{
			pd = new ProgressDialog(LoginActivity.this);
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.setMessage("Loging in...");
			pd.show();
		}
		
                @Override
		protected void onPostExecute(String result)
		{
			if(pd != null && pd.isShowing())
		    {
				pd.dismiss();
			}
			if(result.equals("0"))
			{
				finish();
				startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
			}
			else
			{
				showErrorDialog(getErrorMessage(result));
			}
		}
		
	}
	
	private void showErrorDialog(String message)
	{
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage(message);
		b.setPositiveButton("OK :(", null);
                b.setNeutralButton("Contact Developer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface di, int i) 
                    {
                        //TODO send mail with error message
                    }
                });
		b.create().show();
	}
	
	private String getErrorMessage(String error)
	{
		if(error.equals("failed authentication"))
		{
			return "Wrong username or password!";//TODO this should be R.string....
		}
		else if(error.startsWith("invalid"))
		{
			return "Error: " + error + "\n\nPlease contact developer!";
		}
		else if(error.equals("server is over capacity"))
		{
			return "Server is Over Capacity\nPlease try again in 10-15 seconds.";
		}
		else
		{
			return "Unknown error: " + error + "\n\nPlease contact developer\nMake sure to include this error message!";
		}
	}
}
