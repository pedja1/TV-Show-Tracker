package rs.pedjaapps.tvshowtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.pedjaapps.tvshowtracker.R;

/**
 * Created by pedja on 21.4.15..
 */
public class FragmentMoviesPopular extends Fragment
{
    public static FragmentMoviesPopular newInstance()
    {
        FragmentMoviesPopular fragment = new FragmentMoviesPopular();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_movies_popular, container, false);
        return view;
    }
}
