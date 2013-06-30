package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.R.drawable;
import rs.pedjaapps.tvshowtracker.R.id;
import rs.pedjaapps.tvshowtracker.R.layout;
import rs.pedjaapps.tvshowtracker.model.Agenda;
import rs.pedjaapps.tvshowtracker.model.AgendaItem;
import rs.pedjaapps.tvshowtracker.model.AgendaSection;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.core.*;

public final class AgendaAdapter extends ArrayAdapter<Agenda>
{

    DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	LayoutInflater inflater;
	
	public AgendaAdapter(final Context context)
	{
		super(context, 0);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.noimage)
			.showImageForEmptyUri(R.drawable.noimage)
			.showImageOnFail(R.drawable.noimage)
			.cacheInMemory()
			
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
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
			imageLoader.displayImage("file://"+ai.getBanner(), ((ImageView)view.findViewById(R.id.imgSeriesImage)), options);	
		}
		return view;
	}

	

}
