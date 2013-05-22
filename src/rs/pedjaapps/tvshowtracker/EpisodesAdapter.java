package rs.pedjaapps.tvshowtracker;

import java.text.ParseException;
import java.util.Date;

import android.content.*;
import android.view.*;
import android.widget.*;

public final class EpisodesAdapter extends ArrayAdapter<Episode> {

	LayoutInflater inflater;
	String seriesId;
	
	public EpisodesAdapter(final Context context, String seriesId) {

		super(context, 0);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.seriesId = seriesId;
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
				public void onClick(View v) {
					for(int i = 0; i < EpisodesAdapter.this.getCount(); i++){
						if(!getItem(i).isSection()){
						EpisodeItem ei = (EpisodeItem)getItem(i);
						if(ei.getSeason() == es.getSeason()){
							((CheckBox)EpisodesAdapter.this.getView(i, convertView, parent).findViewById(R.id.chkWatched)).setChecked(true);
							//ei.setWatched(true);
							//db.updateEpisode(ei, ei.getEpisodeId()+"", seriesId);
						}
						}
					}
					EpisodesAdapter.this.notifyDataSetChanged();
				}
			});
		}
		else{
			view  = inflater.inflate(R.layout.details_episodes_row, null);
			final EpisodeItem ei = (EpisodeItem)e;
			((TextView)view.findViewById(R.id.txtEpisodeNumber)).setText(episode(ei));
			((TextView)view.findViewById(R.id.txtTitle)).setText(ei.getEpisodeName());
			CheckBox mark = (CheckBox)view.findViewById(R.id.chkWatched);
			mark.setChecked(ei.isWatched());
			
			mark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					DatabaseHandler db = new DatabaseHandler(getContext());
                    ei.setWatched(isChecked);
					db.updateEpisode(ei, ei.getEpisodeId()+"", seriesId);
					
				}
			});
		}
		
		return view;
	}

	

	public static String episode(EpisodeItem e){
		StringBuilder b = new StringBuilder();
		b.append("S");
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
			}
			else if(new Date().after(firstAired)){
				b.append(" | Aired ").append(Constants.df2.format(firstAired));
			}
		} catch (ParseException e1) {
		}
		
		return b.toString();
	}
	
}
