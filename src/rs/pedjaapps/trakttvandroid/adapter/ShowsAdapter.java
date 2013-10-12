package rs.pedjaapps.trakttvandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import rs.pedjaapps.trakttvandroid.R;
import rs.pedjaapps.trakttvandroid.model.Show;

public final class ShowsAdapter extends ArrayAdapter<Show>
{

	private final int itemLayoutResource;
    DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public ShowsAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.noimage)
			.showImageForEmptyUri(R.drawable.noimage)
			.showImageOnFail(R.drawable.noimage)
			.cacheInMemory()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Show entry = getItem(position);

		
		viewHolder.progressView.setProgress(entry.getPrgWatched());
		viewHolder.upcomingEpisodeView.setText(entry.getNextEpisode());
		imageLoader.displayImage("file://"+entry.getBanner(), viewHolder.bannerView, options);
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
