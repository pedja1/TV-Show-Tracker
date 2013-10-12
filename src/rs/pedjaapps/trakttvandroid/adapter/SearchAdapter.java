package rs.pedjaapps.trakttvandroid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import rs.pedjaapps.trakttvandroid.R;
import rs.pedjaapps.trakttvandroid.model.Show;

public final class SearchAdapter extends ArrayAdapter<Show>
{

	private final int itemLayoutResource;
    Context c;
	public SearchAdapter(final Context context, final int itemLayoutResource)
	{
		
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
		this.c = context;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Show entry = getItem(position);

		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
		viewHolder.titleView.setText(entry.getSeriesName());
		viewHolder.networkView.setText(entry.getNetwork());
		try {
			viewHolder.yearView.setText(df2.format(df.parse(entry.getFirstAired())));
		} catch (ParseException e) {
			viewHolder.yearView.setText("");
		}
		viewHolder.plotView.setText(entry.getOverview());
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

			viewHolder.titleView = (TextView) workingView.findViewById(R.id.title);
			viewHolder.networkView = (TextView) workingView.findViewById(R.id.network);
			viewHolder.plotView = (TextView) workingView.findViewById(R.id.plot);
			//viewHolder.countryView = (TextView) workingView.findViewById(R.id.country);
			viewHolder.yearView = (TextView) workingView.findViewById(R.id.year);
			
			workingView.setTag(viewHolder);

		}
		else
		{
			viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
	}

	private class ViewHolder
	{
		public TextView titleView;
		public TextView networkView;
		public TextView plotView;
		//public TextView countryView;
		public TextView yearView;
	}


}