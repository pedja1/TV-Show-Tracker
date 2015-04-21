package rs.pedjaapps.tvshowtracker.dialog;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;

import android.support.v4.app.DialogFragment;


/**
 * Created by pedja on 29.9.14. 15.36.
 * This class is part of the Preezm
 * Copyright © 2014 ${OWNER}
 */
public class AlertDialogFragment extends DialogFragment
{
    // Global field to contain the dialog
    private Dialog mDialog;

    // Default constructor. Sets the dialog field to null
    public AlertDialogFragment()
    {
        super();
        mDialog = null;
    }

    // Set the dialog to display
    public void setDialog(Dialog dialog)
    {
        mDialog = dialog;
    }

    // Return a Dialog to the DialogFragment.
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return mDialog;
    }
}
