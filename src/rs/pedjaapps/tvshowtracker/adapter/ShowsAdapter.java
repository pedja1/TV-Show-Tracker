package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoScrollingTextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import com.android.volley.cache.DiskLruBasedCache;
import com.android.volley.cache.SimpleImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public final class ShowsAdapter extends ArrayAdapter<Show>
{

    private final int itemLayoutResource;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	SimpleImageLoader mImageFetcher;

    public ShowsAdapter(final Context context, final int itemLayoutResource)
    {
        super(context, 0);
        this.itemLayoutResource = itemLayoutResource;
        options = new DisplayImageOptions.Builder().cloneFrom(MainApp.getInstance().displayImageOptions)
                .showImageForEmptyUri(R.drawable.noimage_poster_actor)
                .showImageOnFail(R.drawable.noimage_poster_actor)
                .showImageOnLoading(R.drawable.noimage_poster_actor)
				.build();
		DiskLruBasedCache.ImageCacheParams cacheParams = new DiskLruBasedCache.ImageCacheParams(getContext().getApplicationContext(), "CacheDirectory");
		cacheParams.setMemCacheSizePercent(0.5f);
		cacheParams.diskCacheEnabled = true;

		mImageFetcher = new SimpleImageLoader(getContext().getApplicationContext(), R.drawable.noimage_poster_actor, cacheParams);
		
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Show show = getItem(position);

        viewHolder.upcomingEpisodeView.setText(show.getTitle());
        //imageLoader.displayImage(show.getImage().getPoster(), viewHolder.ivPoster, options);
		mImageFetcher.get(show.getImage().getPoster(), viewHolder.ivPoster);
        viewHolder.ivMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PopupMenu menu = new PopupMenu(getContext(), viewHolder.ivMore);
                menu.inflate(R.menu.show_options);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.update:
                                break;
                            /*case R.id.updateImages:
                                break;*/
                            case R.id.delete:
                                Utility.showDeleteDialog(getContext(), show, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        MainApp.getInstance().getDaoSession().getShowDao().delete(show);
                                        ((MainActivity)getContext()).refreshShows();
                                    }
                                });
                                break;
                            case R.id.sync:
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        return view;
    }

    private View getWorkingView(final View convertView)
    {
        View workingView;

        if (null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
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

            viewHolder.upcomingEpisodeView = (AutoScrollingTextView) workingView.findViewById(R.id.txtUpcomingEpisode);
            viewHolder.ivPoster = (ImageView) workingView.findViewById(R.id.imgSeriesImage);
            viewHolder.ivMore = (ImageView) workingView.findViewById(R.id.ivMore);

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
        public AutoScrollingTextView upcomingEpisodeView;
        public ImageView ivPoster, ivMore;

    }

}
