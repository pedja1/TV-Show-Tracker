package rs.pedjaapps.tvshowtracker;


import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.core.*;

public final class BannersAdapter extends ArrayAdapter<Show>
{

	private final int itemLayoutResource;
    DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public BannersAdapter(final Context context, final int itemLayoutResource)
	{
		super(context, 0);
		this.itemLayoutResource = itemLayoutResource;
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

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final Show entry = getItem(position);
		imageLoader.displayImage("http://thetvdb.com/banners/"+entry.getBanner(), viewHolder.bannerView, options);
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
		ViewHolder viewHolder = null;


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
