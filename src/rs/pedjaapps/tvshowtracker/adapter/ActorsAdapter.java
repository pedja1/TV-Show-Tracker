package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.utils.ImageManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class ActorsAdapter extends ArrayAdapter<Actor>
{

	private final int itemLayoutResource;
	public ActorsAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Actor actor = getItem(position);
        ImageManager.ImageReference imageReference = new ImageManager.ImageReference(
                actor.getImage(), viewHolder.imageView);
        imageReference.getImageView().setTag(imageReference.getImageName());
        ImageManager.getInstance().getWebImage(imageReference);
        viewHolder.nameView.setText(actor.getName());
        viewHolder.roleView.setText(actor.getRole());
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

			viewHolder.imageView = (ImageView) workingView.findViewById(R.id.image);
            viewHolder.nameView = (TextView) workingView.findViewById(R.id.name);
            viewHolder.roleView = (TextView) workingView.findViewById(R.id.role);
			
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
		public ImageView imageView;
        public TextView nameView;
        public TextView roleView;

	}

}
