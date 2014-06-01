package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Show;

import android.content.*;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.core.*;

public final class ShowsAdapter extends ArrayAdapter<Show>
{

	private final int itemLayoutResource;
    DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public ShowsAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
        options = new DisplayImageOptions.Builder().cloneFrom(MainApp.getInstance().displayImageOptions)
                .showImageForEmptyUri(R.drawable.noimage_actor)
                .showImageOnFail(R.drawable.noimage_actor)
                .showImageOnLoading(R.drawable.noimage_actor).build();
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Show show = getItem(position);

		
		viewHolder.progressView.setProgress(show.getWatchedPercent());
		viewHolder.upcomingEpisodeView.setText(show.getTitle());
		imageLoader.displayImage(show.getImage().getPoster(), viewHolder.ivPoster, options);
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

			viewHolder.progressView = (ProgressBar) workingView.findViewById(R.id.pgrWatched);
			viewHolder.upcomingEpisodeView = (AutoScrollingTextView) workingView.findViewById(R.id.txtUpcomingEpisode);
			viewHolder.ivPoster = (ImageView) workingView.findViewById(R.id.imgSeriesImage);
			
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
		public ProgressBar progressView;
		public AutoScrollingTextView upcomingEpisodeView;
		public ImageView ivPoster;

	}

}
