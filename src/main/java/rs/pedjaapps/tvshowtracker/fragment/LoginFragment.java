package rs.pedjaapps.tvshowtracker.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by pedja on 31.5.14..
 */
public class LoginFragment extends DialogFragment
{
    public static LoginFragment newInstance()
    {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return super.onCreateDialog(savedInstanceState);
    }
}
