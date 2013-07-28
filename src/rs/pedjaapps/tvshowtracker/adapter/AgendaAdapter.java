package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Agenda;
import rs.pedjaapps.tvshowtracker.model.AgendaItem;
import rs.pedjaapps.tvshowtracker.model.AgendaSection;
import rs.pedjaapps.tvshowtracker.utils.ImageManager;

import android.content.*;
import android.view.*;
import android.widget.*;

public final class AgendaAdapter extends ArrayAdapter<Agenda>
{
	
	LayoutInflater inflater;
	
	public AgendaAdapter(final Context context)
	{
		super(context, 0);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		
		final Agenda a = getItem(position);
		final boolean isSection = a.isSection();
		
		View view;
		if(isSection){
			view  = inflater.inflate(R.layout.agenda_section, null);
			final AgendaSection as = (AgendaSection)a;
			((TextView)view.findViewById(R.id.airs)).setText(as.getAirs());
		
		}
		else{
			view  = inflater.inflate(R.layout.agenda_row, null);
			final AgendaItem ai = (AgendaItem)a;
			((TextView)view.findViewById(R.id.txtUpcomingEpisode)).setText(ai.getNextEpisode());
            ImageManager.ImageReference imageReference = new ImageManager.ImageReference(
                    ((AgendaItem) a).getBanner(), ((ImageView)view.findViewById(R.id.imgSeriesImage)));
            imageReference.getImageView().setTag(imageReference.getImageName());
            ImageManager.getInstance().getWebImage(imageReference);
		}
		return view;
	}

	

}
