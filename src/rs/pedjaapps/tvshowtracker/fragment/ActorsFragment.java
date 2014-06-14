package rs.pedjaapps.tvshowtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.adapter.ActorsAdapter;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.Show;

/**
 * Created by pedja on 7.6.14..
 */
public class ActorsFragment extends Fragment
{
    public static final String EXTRA_ACTORS = "actors";
    public static ActorsAdapter adapter;

    public static ActorsFragment newInstance()
    {
        ActorsFragment actorsFragment = new ActorsFragment();
        return actorsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Show show = ((ShowDetailsActivity)getActivity()).getShow();
        List<Actor> actors = new ArrayList<Actor>();
        if(show != null && show.getActors() != null) actors = show.getActors();
        View rootView = inflater.inflate(R.layout.details_actors, container, false);
        adapter = new ActorsAdapter(getActivity(), R.layout.details_actors_row);
        ListView list = (ListView) rootView.findViewById(R.id.lvActors);
        for (Actor a : actors)
        {
            adapter.add(a);
        }

        list.setAdapter(adapter);

        return rootView;
    }
}
