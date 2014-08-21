package rs.pedjaapps.tvshowtracker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Season;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.Comparators;
import rs.pedjaapps.tvshowtracker.utils.Utility;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

/**
 * Created by pedja on 7.6.14..
 */
public class EpisodesFragment extends Fragment
{
    //public static EpisodesAdapter adapter;
	long timeNow;

    public static EpisodesFragment newInstance()
    {
        EpisodesFragment actorsFragment = new EpisodesFragment();
        return actorsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		timeNow = System.currentTimeMillis() / 1000;
        Show show = ((ShowDetailsActivity)getActivity()).getShow();
        List<Episode> episodes = new ArrayList<Episode>();
        if(show != null && show.getEpisodes() != null)episodes = show.getEpisodes();
        View rootView = inflater.inflate(R.layout.details_episodes, container, false);
        //adapter = new EpisodesAdapter(getActivity());
        //ListView list = (ListView) rootView.findViewById(R.id.list);
		LinearLayout llContent = (LinearLayout)rootView.findViewById(R.id.llContent);

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
                        inflateSeason(inflater, llContent, season);
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
            inflateSeason(inflater, llContent, season);
        }
        /*list.setAdapter(adapter);
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
            /*}

        });*/
        return rootView;
    }

	private void inflateSeason(LayoutInflater inflater, LinearLayout llContent, final Season season)
	{
		LinearLayout seasonView = (LinearLayout) inflater.inflate(R.layout.layout_season, llContent, false);
		
		TextView tvSeason = (TextView) seasonView.findViewById(R.id.tvSeason);
		TextView tvMarkAll = (TextView) seasonView.findViewById(R.id.tvMarkAll);
		LinearLayout llEpisodes = (LinearLayout) seasonView.findViewById(R.id.llEpisodes);
		
		final List<CheckBox> cbs = new ArrayList<>();
		
		for(final Episode e : season.getEpisodes())
		{
			RelativeLayout rlEpisodeLayout = (RelativeLayout) inflater.inflate(R.layout.episode_layout, llEpisodes, false);
			rlEpisodeLayout.setOnClickListener(new OnClickListener()
			{
					@Override
					public void onClick(View p1)
					{
						// TODO: show episode details
					}
			});
			TextView tvTitle = (TextView)rlEpisodeLayout.findViewById(R.id.tvTitle);
		    final CheckBox cbWatched = (CheckBox)rlEpisodeLayout.findViewById(R.id.cbWatched);
			cbs.add(cbWatched);
			TextView tvAirs = (TextView)rlEpisodeLayout.findViewById(R.id.tvAirs);
			
			tvTitle.setText(e.getTitle() + " | S" + (e.getSeason() < 10 ? "0" : "") + e.getSeason() + "E" + (e.getSeason() < 10 ? "0" : "") + e.getEpisode());
            String airTime = Utility.generateEpisodeAiredTime(e.getFirst_aired());
            if(airTime == null)
            {
                tvAirs.setText("");
                tvAirs.setVisibility(View.GONE);
            }
            else
            {
                tvAirs.setText(airTime);
                tvAirs.setVisibility(View.VISIBLE);
            }

            if (e.getFirst_aired() != 0 && timeNow > e.getFirst_aired())
            {
                cbWatched.setEnabled(true);
                cbWatched.setChecked(e.isWatched());
                cbWatched.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							e.setWatched(cbWatched.isChecked());
							MainApp.getInstance().getDaoSession().getEpisodeDao().insertOrReplace(e);
						}
					});
            }
            else
            {
                cbWatched.setEnabled(false);
                cbWatched.setChecked(false);
            }
			llEpisodes.addView(rlEpisodeLayout);
		}
		
		tvSeason.setText(getString(R.string.season, season.getSeason()));
        tvMarkAll.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					int index = 0;
					for(Episode e : season.getEpisodes())
					{
						e.setWatched(true);
						cbs.get(index).setChecked(true);
						index++;
					}
					MainApp.getInstance().getDaoSession().getEpisodeDao().insertOrReplaceInTx(season.getEpisodes());
				}
			});
		
		llContent.addView(seasonView);
	}
}
