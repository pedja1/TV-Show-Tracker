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
	public int getItemViewType(int pos)
	{
		if(getItem(pos).isSection()) return 1;
		else if(!getItem(pos).isSection()) return 0;
		else return -1;
	}

	@Override
	public int getItemViewTypeCount()
	{
        return 2;
	}
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		final Agenda a = getItem(position);
		int itemType = getItemViewType(position);
		
		switch(itemType)
		{
			case 0:
			    View itemView = convertView;
			    ItemHolder itemHolder;
				if(itemView == null || itemView.getId() != R.id.item_container)
				{
					itemView = inflater.inflate(R.layout.agenda_row, null);
					itemHolder = new ItemHolder();
					itemHolder.episodeInfo = (TextView) itemView.findViewById(R.id.txtUpcomingEpisode);
					itemHolder.banner = (ImageView)itemView.findViewById(R.id.imgSeriesImage);
					itemView.setTag(itemHolder);
				}
				else
				{
					itemHolder = (AgendaAdapter.ItemHolder) convertView.getTag();
				}
				AgendaItem ai = (AgendaItem)a;
				itemHolder.episodeInfo.setText(ai.getNextEpisode());
				imageLoader.displayImage("file://"+ai.getBanner(), itemHolder.banner, options);
				return itemView;
			case 1:
			    View sectionView = convertView;
			    SectionHolder sectionHolder;
				if(sectionView == null || sectionView.getId() != R.id.section_container)
				{
					sectionView = inflater.inflate(R.layout.agenda_section, null);
					sectionHolder = new SectionHolder();
					sectionHolder.showName = (TextView) sectionView.findViewById(R.id.showName);
					sectionView.setTag(sectionHolder);
				}
				else
				{
					sectionHolder = (SectionHolder) convertView.getTag();
				}
				AgendaSection as = (AgendaSection)a;
				sectionHolder.showName.setText(as.getShowName());
				return sectionView;
		}
		return convertView;
	}

	public class SectionHolder
	{
		TextView showName;
	}
	
	public class ItemHolder
	{
		TextView episodeInfo;
		ImageView banner;
	}

}
