package rs.pedjaapps.tvshowtracker.adapter;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.ImageManager;

import android.content.*;
import android.view.*;
import android.widget.*;
public final class ShowsAdapter extends ArrayAdapter<Show>
{

	private final int itemLayoutResource;
	public ShowsAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Show entry = getItem(position);

		
		viewHolder.progressView.setProgress(entry.getPrgWatched());
		viewHolder.upcomingEpisodeView.setText(entry.getNextEpisode());

        ImageManager.ImageReference imageReference = new ImageManager.ImageReference(
                entry.getBanner(), viewHolder.bannerView);
        imageReference.getImageView().setTag(imageReference.getImageName());
        ImageManager.getInstance().getWebImage(imageReference);

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
			viewHolder.upcomingEpisodeView = (TextView) workingView.findViewById(R.id.txtUpcomingEpisode);
			viewHolder.bannerView = (ImageView) workingView.findViewById(R.id.imgSeriesImage);
			
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
		public TextView upcomingEpisodeView;
		public ImageView bannerView;

	}

}
