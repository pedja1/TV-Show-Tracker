package rs.pedjaapps.trakttvandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rs.pedjaapps.trakttvandroid.R;
import rs.pedjaapps.trakttvandroid.model.Episode;
import rs.pedjaapps.trakttvandroid.model.EpisodeItem;
import rs.pedjaapps.trakttvandroid.model.EpisodeSection;
import rs.pedjaapps.trakttvandroid.utils.Constants;
import rs.pedjaapps.trakttvandroid.utils.DatabaseHandler;
import rs.pedjaapps.trakttvandroid.utils.Tools;

public final class EpisodesAdapter extends ArrayAdapter<Episode> {

	LayoutInflater inflater;
	String seriesId;
	DatabaseHandler db;
    boolean checkChangeListenerEnabled = true;
	
	public EpisodesAdapter(final Context context, String seriesId, DatabaseHandler db) {

		super(context, 0);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.seriesId = seriesId;
		this.db = db;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		
		final Episode e = getItem(position);
		final boolean isSection = e.isSection();
		//final View view = getWorkingView(convertView, isSection);
		//final ViewHolder viewHolder = getViewHolder(view, isSection);
		View view;
		if(isSection){
			view  = inflater.inflate(R.layout.details_episodes_section, null);
			final EpisodeSection es = (EpisodeSection)e;
			((TextView)view.findViewById(R.id.season)).setText("SEASON "+es.getSeason());
			((TextView)view.findViewById(R.id.markAllWatched)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v)
                {
                    checkChangeListenerEnabled = false;
                    List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
					for(int i = 0; i < EpisodesAdapter.this.getCount(); i++)
                    {
						if(!getItem(i).isSection())
                        {
						EpisodeItem ei = (EpisodeItem)getItem(i);
						if(ei.getSeason() == es.getSeason())
                        {
							((CheckBox)EpisodesAdapter.this.getView(i, convertView, parent).findViewById(R.id.chkWatched)).setChecked(true);
							ei.setWatched(true);
							episodes.add(ei);
						}
						}
					}
					EpisodesAdapter.this.notifyDataSetChanged();
                    db.insertEpisodes(episodes);
                    checkChangeListenerEnabled = true;
                    Tools.setRefresh(true);
				}
			});
		}
		else{
			view  = inflater.inflate(R.layout.details_episodes_row, null);
			final EpisodeItem ei = (EpisodeItem)e;
			String[] episode = episode(ei);
			((TextView)view.findViewById(R.id.txtEpisodeNumber)).setText(episode[0]);
			((TextView)view.findViewById(R.id.txtTitle)).setText(ei.getEpisodeName());
			CheckBox mark = (CheckBox)view.findViewById(R.id.chkWatched);
			mark.setChecked(ei.isWatched());
			if(episode[1].equals("1")){
				mark.setVisibility(View.GONE);
			}
			
			mark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(checkChangeListenerEnabled)
                    {
                    List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
                    ei.setWatched(isChecked);
                    episodes.add(ei);
					db.insertEpisodes(episodes);
					if(!Tools.isRefresh())
					Tools.setRefresh(true);
                    }
				}
			});
		}
		
		return view;
	}

	

	public static String[] episode(EpisodeItem e){
		StringBuilder b = new StringBuilder();
		b.append("S");
		String s = "0";
		if(e.getSeason()<=10){
			b.append("0").append(e.getSeason());
		}
		else{
			b.append(e.getSeason());
		}
		b.append("E");
		if(e.getEpisode()<=10){
			b.append("0").append(e.getEpisode());
		}
		else{
			b.append(e.getEpisode());
		}
		Date firstAired;
		try {
			firstAired = Constants.df.parse(e.getFirstAired());
			if((new Date().getTime() / (1000*60*60*24)) == (firstAired.getTime()/ (1000*60*60*24))){
				b.append(" | Airs today");
			}
			else if(new Date().before(firstAired)){
				b.append(" | Airs ").append(Constants.df2.format(firstAired));
				s = "1";
			}
			else if(new Date().after(firstAired)){
				b.append(" | Aired ").append(Constants.df2.format(firstAired));
			}
		} catch (ParseException e1) {
			b.append(" | TBD");
			s = "1";
		}
		
		return new String[] {b.toString(), s};
	}
	
}