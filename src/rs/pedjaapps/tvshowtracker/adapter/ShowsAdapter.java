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
import android.widget.TextView;
import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;
import rs.pedjaapps.tvshowtracker.MainActivity;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;
import rs.pedjaapps.tvshowtracker.model.ShowNoDao;

public final class ShowsAdapter extends ArrayAdapter<Show>
{
	SimpleImageLoader mImageFetcher;
    public static final float IMAGE_RATIO = 0.7f;

    public ShowsAdapter(final Context context)
    {
        super(context, 0);
		mImageFetcher = new SimpleImageLoader(getContext().getApplicationContext(), R.drawable.noimage_poster_actor, MainApp.getInstance().cacheParams);
        //set max image size to screen width divided by number of columns and multiplied by aspect ratio(so that we actually get height of poster)
        mImageFetcher.setMaxImageSize((int) ((DisplayManager.screenWidth / context.getResources().getInteger(R.integer.main_column_num)) * IMAGE_RATIO));
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Show show = getItem(position);

        viewHolder.upcomingEpisodeView.setText(show.getTitle());
        viewHolder.ivPoster.setDefaultImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivPoster.setErrorImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivPoster.setImageUrl(show.getImage().getPoster(), mImageFetcher);
		//mImageFetcher.get(show.getImage().getPoster(), viewHolder.ivPoster);
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
                                        Utility.deleteShowFromDb(show);
                                        //((MainActivity)getContext()).refreshShows();
										//TODO refresh shows
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
		viewHolder.tvWatchedPercent.setText(calcWatchedPerc(show));
		if(show instanceof ShowNoDao)
		{
			viewHolder.tvFavorite.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.tvFavorite.setVisibility(View.VISIBLE);
		}
        return view;
    }
	
	private String calcWatchedPerc(Show show)
	{
		int count = show.getEpisodes().size();
		int watched = 0;
		for(Episode e : show.getEpisodes())
		{
			if(e.isWatched() || e.getSeason() == 0)watched++;
		}
		return (watched == 0 ? 0 : (int)(100.0f / ((float)count / (float)watched))) + "%";
	}

    private View getWorkingView(final View convertView)
    {
        View workingView;

        if (null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(R.layout.shows_list_row, null);
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
            viewHolder.ivPoster = (NetworkImageViewPlus) workingView.findViewById(R.id.imgSeriesImage);
            viewHolder.ivMore = (ImageView) workingView.findViewById(R.id.ivMore);
			viewHolder.tvWatchedPercent = (TextView) workingView.findViewById(R.id.tvWatchedPercent);
			viewHolder.tvFavorite = (TextView) workingView.findViewById(R.id.tvFavorite);
			
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
        public NetworkImageViewPlus ivPoster;
        ImageView ivMore;
		TextView tvWatchedPercent, tvFavorite;

    }

}
