package rs.pedjaapps.tvshowtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.adapter.EpisodesAdapter;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Season;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.Comparators;

/**
 * Created by pedja on 7.6.14..
 */
public class EpisodesFragment extends Fragment
{
    public static EpisodesAdapter adapter;

    public static EpisodesFragment newInstance()
    {
        EpisodesFragment actorsFragment = new EpisodesFragment();
        return actorsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Show show = ((ShowDetailsActivity)getActivity()).getShow();
        List<Episode> episodes = new ArrayList<Episode>();
        if(show != null && show.getEpisodes() != null)episodes = show.getEpisodes();
        View rootView = inflater.inflate(R.layout.details_episodes, container, false);
        adapter = new EpisodesAdapter(getActivity());
        ListView list = (ListView) rootView.findViewById(R.id.list);

        List<Integer> seasonIds = new ArrayList<Integer>();
        List<Episode> sEpisodes = new ArrayList<Episode>();
        Season season = null;
        for (Episode episode : episodes)
        {
            if (episode.getSeason() != 0)
            {
                if (!seasonIds.contains(episode.getSeason()))
                {
                    if(season != null)
                    {
                        Collections.sort(sEpisodes, new Comparators.EpisodeNumberComparator(false));
                        season.setEpisodes(sEpisodes);
                        adapter.add(season);
                        sEpisodes = new ArrayList<Episode>();
                    }
                    season = new Season();
                    season.setSeason(episode.getSeason());
                    seasonIds.add(episode.getSeason());
                }
                sEpisodes.add(episode);
            }
        }
        if(season != null)
        {
            Collections.sort(sEpisodes, new Comparators.EpisodeNumberComparator(false));
            season.setEpisodes(sEpisodes);
            adapter.add(season);
        }
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3)
            {

                /*if (adapter.getItem(position).isEpisode())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());

                    builder.setTitle(((EpisodeItem) adapter
                            .getItem(position)).getEpisodeName());
                    String plot = ((EpisodeItem) adapter
                            .getItem(position)).getOverview();
                    builder.setMessage(plot.length() > 0 ? plot : "Plot not available");

                    builder.setPositiveButton(
                            getResources().getString(android.R.string.ok),
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            });

                    AlertDialog alert = builder.create();

                    alert.show();
                    //TODO open episode details activity
                }*/
            }

        });
        return rootView;
    }
}
