package rs.pedjaapps.trakttvandroid.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import rs.pedjaapps.trakttvandroid.R;
import rs.pedjaapps.trakttvandroid.model.Profile;

public final class ProfilesAdapter extends ArrayAdapter<Profile>
{

	private final int itemLayoutResource;
	String profile;
	public ProfilesAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
		profile = p.getString("profile", "Default");
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		Profile p = getItem(position);
		viewHolder.nameView.setText(p.getName());
		if(p.isActive()){
			viewHolder.activeView.setVisibility(View.VISIBLE);
		}
		else{
			viewHolder.activeView.setVisibility(View.GONE);
		}
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

			viewHolder.activeView = (ImageView) workingView.findViewById(R.id.active);
            viewHolder.nameView = (TextView) workingView.findViewById(R.id.name);
           
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
		public ImageView activeView;
        public TextView nameView;

	}

}
