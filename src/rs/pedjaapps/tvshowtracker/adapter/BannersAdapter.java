package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.ShowOld;

public final class BannersAdapter extends ArrayAdapter<ShowOld>
{

	private final int itemLayoutResource;
	public BannersAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final ShowOld entry = getItem(position);
        Ion.with(viewHolder.bannerView)
                .placeholder(R.drawable.noimage_banner)
                .error(R.drawable.noimage_banner)
                .load("http://thetvdb.com/banners/"+entry.getBanner());
		return view;
	}

	private View getWorkingView(final View convertView)
	{
		View workingView;

		if (null == convertView)
		{
			final Context context = getContext();
			final LayoutInflater inflater = (LayoutInflater)context.getSystemService
			(Context.LAYOUT_INFLATER_SERVICE);

			workingView = inflater.inflate(itemLayoutResource, null);
		}
		else
		{
			workingView = convertView;
		}

		return workingView;
	}

	private ViewHolder getViewHolder(final View workingView)
	{
		final Object tag = workingView.getTag();
		ViewHolder viewHolder;


		if (null == tag || !(tag instanceof ViewHolder))
		{
			viewHolder = new ViewHolder();

			viewHolder.bannerView = (ImageView) workingView.findViewById(R.id.banner);
			
			workingView.setTag(viewHolder);

		}
		else
		{
			viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
	}

	 class ViewHolder
	{
		public ImageView bannerView;

	}

}
