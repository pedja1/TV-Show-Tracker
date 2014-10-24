package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;

import java.util.List;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowNoDao;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;
import rs.pedjaapps.tvshowtracker.widget.AutoScrollingTextView;

public final class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder>
{
	SimpleImageLoader mImageFetcher;
    public static final float IMAGE_RATIO = 1.6f;

    Context context;

    List<Show> shows;

    public OnItemClickListener onItemClickListener;

    int posterWidth;

    public ShowsAdapter(final Context context, List<Show> shows)
    {
        this.context = context;
        this.shows = shows;

        posterWidth = (DisplayManager.screenWidth / context.getResources().getInteger(R.integer.main_column_num));

		mImageFetcher = new SimpleImageLoader(context.getApplicationContext(), R.drawable.noimage_poster_actor, MainApp.getInstance().cacheParams);
        //set max image size to screen width divided by number of columns and multiplied by aspect ratio(so that we actually get height of poster)
        mImageFetcher.setMaxImageSize((int) (posterWidth * IMAGE_RATIO));

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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.shows_list_row, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        final Show show = shows.get(position);

        viewHolder.tvTitle.setText(show.getTitle());
        if(show.getUpcomingEpisode() != null)
		{
			viewHolder.upcomingEpisodeView.setVisibility(View.VISIBLE);
			viewHolder.upcomingEpisodeView.setText(Utility.generateUpcomingEpisodeText(show.getUpcomingEpisode(), false));
		}
		else
		{
			viewHolder.upcomingEpisodeView.setVisibility(View.GONE);
		}

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.ivPoster.getLayoutParams();
        params.width = posterWidth;
        params.height = (int) (posterWidth * IMAGE_RATIO);
        viewHolder.ivPoster.setLayoutParams(params);

        viewHolder.ivPoster.setDefaultImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivPoster.setErrorImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivPoster.setImageUrl(show.getImage() != null ? Utility.generatePosterUrl(Utility.ImageSize.LARGE_POSTER, show.getImage().getPoster()) : "", mImageFetcher);
        //mImageFetcher.get(show.getImage().getPoster(), viewHolder.ivPoster);

        if(show instanceof ShowNoDao)
        {
            viewHolder.tvFavorite.setVisibility(View.GONE);
            viewHolder.tvWatchedPercent.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.tvFavorite.setVisibility(View.VISIBLE);
            viewHolder.tvWatchedPercent.setText(calcWatchedPerc(show));
            viewHolder.tvWatchedPercent.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onItemClickListener != null)onItemClickListener.onItemClick(show, position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return shows.size();
    }

    public boolean isEmpty()
    {
        return shows.isEmpty();
    }

    public void clear()
    {
        shows.clear();
    }

    public void add(Show s)
    {
        shows.add(s);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public AutoScrollingTextView upcomingEpisodeView;
        public NetworkImageViewPlus ivPoster;
        //ImageView ivMore;
		TextView tvWatchedPercent, tvFavorite, tvTitle;

        public ViewHolder(View itemView)
        {
            super(itemView);
            upcomingEpisodeView = (AutoScrollingTextView) itemView.findViewById(R.id.txtUpcomingEpisode);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivPoster = (NetworkImageViewPlus) itemView.findViewById(R.id.imgSeriesImage);
            //ivMore = (ImageView) itemView.findViewById(R.id.ivMore);
            tvWatchedPercent = (TextView) itemView.findViewById(R.id.tvWatchedPercent);
            tvFavorite = (TextView) itemView.findViewById(R.id.tvFavorite);
        }
    }

    public OnItemClickListener getOnItemClickListener()
    {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener
    {
        public void onItemClick(Show show, int position);
    }

    public static class Decorator extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            int margin =  view.getContext().getResources().getDimensionPixelOffset(R.dimen.dp3);
            int columnCount = view.getContext().getResources().getInteger(R.integer.main_column_num);
            int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewPosition();
            int remainder = position % columnCount;
            if (position % columnCount == 0)
            {
                outRect.set(0, 0, margin, margin);
            }
            else if (position % columnCount == columnCount - 1)
            {
                outRect.set(margin, 0, 0, margin);
            }
            else
            {
                outRect.set(0, 0, 0, margin);
            }
        }
    }

}
