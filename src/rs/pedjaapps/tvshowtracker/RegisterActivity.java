package rs.pedjaapps.tvshowtracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FloatLabeledEditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.network.PostParams;
import rs.pedjaapps.tvshowtracker.utils.Utility;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AbsUploadActivity implements LoaderCallbacks<Cursor>, OnClickListener
{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;

    // UI references.
    private FloatLabeledEditText edEmail, edPassword, edPasswordRepeat, edLastName, edFirstName;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView ivAvatar;
    private File avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBar();

        // Set up the login form.
        edEmail = (FloatLabeledEditText) findViewById(R.id.email);
        populateAutoComplete();

        edPassword = (FloatLabeledEditText) findViewById(R.id.password);
        edPasswordRepeat = (FloatLabeledEditText)findViewById(R.id.passwordRepeat);
        edFirstName = (FloatLabeledEditText)findViewById(R.id.edFirstName);
        edLastName = (FloatLabeledEditText)findViewById(R.id.edLastName);
        edLastName.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.register || id == EditorInfo.IME_NULL)
                {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        Button btnPickPhoto = (Button)findViewById(R.id.btnPickPhoto);
        Button btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
        btnPickPhoto.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        ivAvatar = (ImageView)findViewById(R.id.ivAvatar);
    }

    private void populateAutoComplete()
    {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar()
    {
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegistration()
    {
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        edEmail.setError(null);
        edPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        String passwordRepeat = edPasswordRepeat.getText().toString();
        String firstName = edFirstName.getTextString();
        String lastName = edLastName.getTextString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password.
        if (!isPasswordValid(password))
        {
            edPassword.setError(getString(R.string.error_invalid_password));
            focusView = edPassword;
            cancel = true;
        }
        if (!isPasswordValid(passwordRepeat))
        {
            edPasswordRepeat.setError(getString(R.string.error_invalid_password));
            focusView = edPassword;
            cancel = true;
        }
        if (password != null && passwordRepeat != null && !password.equals(passwordRepeat))
        {
            edPassword.setError(getString(R.string.error_passwords_dont_match));
            focusView = edPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            edEmail.setError(getString(R.string.error_field_required));
            focusView = edEmail;
            cancel = true;
        }
        else if (!Utility.isEmailValid(email))
        {
            edEmail.setError(getString(R.string.error_invalid_email));
            focusView = edEmail;
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
            mAuthTask = new UserRegisterTask(email, password, firstName, lastName, avatar);
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
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    @Override
    protected void onPhotoSelected(String imagePath)
    {
        avatar = new File(imagePath);
        ImageLoader.getInstance().displayImage("file://" + avatar.getAbsolutePath(), ivAvatar);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnRegister:
                attemptRegistration();
                break;
            case R.id.btnTakePhoto:
                startTakePhotoActivity();
                break;
            case R.id.btnPickPhoto:
                startPickPhotoActivity();
                break;
        }
    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        edEmail.getEditText().setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, JSONUtility.Response>
    {

        private final String mEmail, mPassword, mLastName, mFirstName;
        private final File mAvatar;

        UserRegisterTask(String email, String password, String firstName, String lastName, File avatar)
        {
            mEmail = email;
            mPassword = password;
            mFirstName = firstName;
            mLastName = lastName;
            mAvatar = avatar;
        }

        @Override
        protected JSONUtility.Response doInBackground(Void... params)
        {
            PostParams postParams = new PostParams();
            postParams.setParamsForLogin(mEmail, new String(Hex.encodeHex(DigestUtils.sha(mPassword))));
            return JSONUtility.parseLoginResponse(postParams);
        }

        @Override
        protected void onPostExecute(final JSONUtility.Response response)
        {
            mAuthTask = null;
            showProgress(false);

            if(!response.getStatus())
            {
                if("login_invalid".equals(response.getErrorCode()))
                {
                    edPassword.setError(getString(R.string.error_incorrect_password));
                    edPassword.requestFocus();
                }
                else
                {
                    Utility.showToast(MainApp.getContext(), response.getErrorMessage());
                }
            }
            else
            {
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



