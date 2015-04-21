package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.view.*;
import rs.pedjaapps.tvshowtracker.model.*;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by pedja on 11/28/13 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 *
 * @author Predrag Čokulov
 */
public abstract class AbsActivity extends ActionBarActivity implements View.OnClickListener
{
    boolean mVisible;
    ProgressDialog mGeneralProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MainApp.getInstance().setCurrentActivity(this);
        mVisible = true;
    }

    @Override
    protected void onPause()
    {
        super.onStop();
        MainApp.getInstance().setCurrentActivity(null);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mVisible = false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public DaoSession getDaoSession()
    {
        return MainApp.getInstance().getDaoSession();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }
    }

    /**
     * Returns ProgressDialog, if {@link #mGeneralProgressDialog} is null it will instantiate it
     * Message of ProgressDialog is predefined
     */
    public ProgressDialog getProgressDialog(String message)
    {
        if (mGeneralProgressDialog == null)
        {
            mGeneralProgressDialog = new ProgressDialog(this);
        }
        mGeneralProgressDialog.setMessage(message);
        return mGeneralProgressDialog;
    }

    /**
     * calls {@link #getProgressDialog(String)} with predefined message("Please wait...")
     */
    public ProgressDialog getProgressDialog()
    {
        return getProgressDialog(getString(R.string.please_wait));
    }

    /**
     * Call {@link android.app.ProgressDialog#dismiss()} on {@link #mGeneralProgressDialog} if not null
     */
    public void dismissProgressDialog()
    {
        if (mGeneralProgressDialog != null) mGeneralProgressDialog.dismiss();
    }

    public boolean isVisible()
    {
        return mVisible;
    }

    protected void setTransactionAnimation(FragmentTransaction transaction, boolean toLeft)
    {
        transaction.setCustomAnimations(toLeft ? R.anim.slide_in_left : R.anim.slide_in_right, toLeft ? R.anim.slide_out_right : R.anim.slide_out_left);
    }

    protected boolean getSlideDirection(View activeTab, View newTab)
    {
        return !(activeTab == null || newTab == null) && newTab.getLeft() < activeTab.getLeft();
    }
    
}
