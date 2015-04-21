package rs.pedjaapps.tvshowtracker.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import rs.pedjaapps.tvshowtracker.R;

/**
 * Created by pedja on 21.4.15..
 */
public abstract class AbsPagerFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_movies, container, false);

        ViewPager mPager = (ViewPager)view.findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)view.findViewById(R.id.tabs);
        tabs.setViewPager(mPager);
        return view;
    }

    protected abstract Fragment getFragmentForViewPager(int position);
    protected abstract CharSequence getPageTitle(int position);
    protected abstract int getPageCount();

    public static class PagerAdapter extends FragmentPagerAdapter
    {
        @NonNull AbsPagerFragment fragment;

        public PagerAdapter(@NonNull AbsPagerFragment fragment)
        {
            super(fragment.getChildFragmentManager());
            this.fragment = fragment;
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragment.getFragmentForViewPager(position);
        }

        @Override
        public int getCount()
        {
            return fragment.getPageCount();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return fragment.getPageTitle(position);
        }
    }
}
