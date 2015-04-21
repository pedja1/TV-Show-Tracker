package rs.pedjaapps.tvshowtracker.fragment;

import android.support.v4.app.Fragment;

import rs.pedjaapps.tvshowtracker.R;

/**
 * Created by pedja on 21.4.15..
 */
public class FragmentMovies extends AbsPagerFragment
{
    private static final int[] titles = {R.string.popular, R.string.trending, R.string.related};

    public static FragmentMovies newInstance()
    {
        FragmentMovies fragment = new FragmentMovies();
        return fragment;
    }


    @Override
    protected Fragment getFragmentForViewPager(int position)
    {
        switch (position)
        {
            case 0:
                return FragmentMoviesPopular.newInstance();
            case 1:
                return FragmentMoviesTrending.newInstance();
            case 2:
                return FragmentMoviesRelated.newInstance();
        }
        return null;
    }

    @Override
    protected CharSequence getPageTitle(int position)
    {
        if(position > titles.length - 1)
        {
            return null;
        }
        return getString(titles[position]);
    }

    @Override
    protected int getPageCount()
    {
        return titles.length;
    }
}
