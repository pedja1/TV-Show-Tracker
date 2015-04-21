package rs.pedjaapps.tvshowtracker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Locale;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.fragment.ActorsFragment;
import rs.pedjaapps.tvshowtracker.fragment.EpisodesFragment;
import rs.pedjaapps.tvshowtracker.fragment.OverviewFragment;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.Show;

/**
 * Created by pedja on 7.6.14..
 */
public class ShowDetailsPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public ShowDetailsPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = null;
        switch (position)
        {
            case 2:
                fragment = ActorsFragment.newInstance();
                break;
            case 0:
                fragment = OverviewFragment.newInstance();
                break;
            case 1:
                fragment = EpisodesFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        // Show 3 total pages.
        return 3;
    }

    /*@Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch (position)
        {
            case 2:
                return context.getString(R.string.title_actors).toUpperCase(l);
            case 0:
                return context.getString(R.string.title_overview).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_episodes).toUpperCase(l);
        }
        return null;
    }*/
}
