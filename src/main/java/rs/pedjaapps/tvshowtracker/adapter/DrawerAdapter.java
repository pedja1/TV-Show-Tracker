package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.R;

import android.content.*;
import android.view.*;
import android.widget.*;

import rs.pedjaapps.tvshowtracker.model.*;

public final class DrawerAdapter extends ArrayAdapter<DrawerItem>
{

	private final int itemLayoutResource;
	
	public DrawerAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final DrawerItem entry = getItem(position);

		viewHolder.textView.setText(entry.getText());
		viewHolder.iconView.setImageResource(entry.getResId());
		return view;
	}

	private View getWorkingView(final View convertView)
	{
		View workingView = null;

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
        
		    viewHolder.textView = (TextView) workingView.findViewById(R.id.text);
			viewHolder.iconView = (ImageView) workingView.findViewById(R.id.icon);
			
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
		public TextView textView;
		public ImageView iconView;
	}

}
