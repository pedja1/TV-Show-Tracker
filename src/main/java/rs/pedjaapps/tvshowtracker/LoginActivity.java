package rs.pedjaapps.tvshowtracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.Utility;
import rs.pedjaapps.tvshowtracker.widget.FButton;
import rs.pedjaapps.tvshowtracker.widget.FloatLabeledEditText;


/**
 * A login screen that offers login via email/password.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class LoginActivity extends Activity implements OnClickListener
{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*if (hasKat())
        {
            setTheme(android.R.style.Theme_Holo_NoActionBar);
        }
        else
        {
            setTheme(android.R.style.Theme_Holo_NoActionBar);
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Set up the login form.
        mEmailView = (FloatLabeledEditText) findViewById(R.id.email);

        mPasswordView = (FloatLabeledEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin(true);
                    return true;
                }
                return false;
            }
        });

        FButton mEmailSignInButton = (FButton) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin(true);
            }
        });
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        TextView tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
    }

    private boolean hasKat()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(boolean login)
    {
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password.
        if (!isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show)
    {
        pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvSkip:
                finish();
                break;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, JSONUtility.Response>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected JSONUtility.Response doInBackground(Void... params)
        {
            //PostParams postParams = new PostParams();
            //postParams.setParamsForLogin(mEmail, new String(Hex.encodeHex(DigestUtils.sha(mPassword)))/*AeSimpleSHA1.SHA1(mPassword)*/);
            //return JSONUtility.parseLoginResponse(postParams);
			return null;
        }

        @Override
        protected void onPostExecute(final JSONUtility.Response response)
        {
            mAuthTask = null;
            showProgress(false);

            /*if(!response.getStatus())
            {
                Utility.showToast(MainApp.getContext(), response.getErrorMessage());
            }
            else
            {
                if(!TextUtils.isEmpty(response.getErrorMessage()))
                {
                    Utility.showMessageAlertDialog(LoginActivity.this, response.getErrorMessage(), null, null);
                }
                else
                {
                    setResult(RESULT_OK);
                    finish();
                }
            }*/
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



