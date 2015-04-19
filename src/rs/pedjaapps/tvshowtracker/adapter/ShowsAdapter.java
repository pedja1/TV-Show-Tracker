package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;

import java.util.Arrays;
import java.util.List;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.fragment.ShowGridFragment;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public final class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder>
{
	SimpleImageLoader mImageFetcher;
    public static final float IMAGE_RATIO = 1.5f;

    Context context;
    ShowGridFragment fragment;

    List<Show> shows;

    public OnItemClickListener onItemClickListener;

    int posterWidth;

    public ShowsAdapter(ShowGridFragment fragment, List<Show> shows)
    {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.shows = shows;

        posterWidth = (DisplayManager.screenWidth / context.getResources().getInteger(R.integer.main_column_num));

		mImageFetcher = new SimpleImageLoader(context.getApplicationContext(), MainApp.getInstance().cacheParams);
        mImageFetcher.setDefaultDrawable(R.drawable.no_image);
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

        View view = inflater.inflate(R.layout.shows_list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        final Show show = shows.get(position);

        viewHolder.tvNextEpisode.setText(show.getTitle());
        /*if(show.getUpcomingEpisode() != null)
		{
			viewHolder.tvNextEpisode.setText(Utility.generateUpcomingEpisodeText(show.getUpcomingEpisode(), false));
		}
		else
		{
            viewHolder.tvNextEpisode.setText(R.string.next_ep_no_info);
		}*/

        viewHolder.ivPoster.setMinimumHeight((int) (posterWidth * IMAGE_RATIO));
        /*viewHolder.ivPoster.post(new Runnable()
        {
            public void run()
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.ivPoster.getLayoutParams();
                //params.width = posterWidth;
                //params.height = (int) (posterWidth * IMAGE_RATIO);
                //viewHolder.ivPoster.setLayoutParams(params);
                viewHolder.ivPoster.setMinimumHeight((int) (params.width * IMAGE_RATIO));
            }
        });*/

        viewHolder.ivPoster.setDefaultImageResId(R.drawable.no_image);
        viewHolder.ivPoster.setErrorImageResId(R.drawable.no_image);
        viewHolder.ivPoster.setImageUrl(show.getImage() != null ? show.getImage().getPoster() : "", mImageFetcher);
        /*viewHolder.ivPoster.setImageListener(new Response.Listener<BitmapDrawable>()
        {
            @Override
            public void onResponse(BitmapDrawable response)
            {
                Palette.generateAsync(response.getBitmap(), new Palette.PaletteAsyncListener()
                {
                    public void onGenerated(Palette palette)
                    {
                        int color = palette.getDarkVibrantColor(context.getResources().getColor(R.color.primary));
                        viewHolder.llTitleContainer.setBackgroundColor(color);
                        show.setPosterMainColor(color);
                    }
                });
            }
        });
        if(show.getPosterMainColor() != -1)viewHolder.llTitleContainer.setBackgroundColor(show.getPosterMainColor());
        //mImageFetcher.get(show.getImage().getPoster(), viewHolder.ivPoster);*/

        //TODO add to fav, is in fav

            viewHolder.tvFavorite.setVisibility(View.GONE);
            viewHolder.tvWatchedPercent.setVisibility(View.GONE);
            viewHolder.ivAdd.setVisibility(View.VISIBLE);
            if (show.isShowAdded())
            {
                viewHolder.ivAdd.setImageResource(R.drawable.ic_action_check);
                viewHolder.ivAdd.setEnabled(false);
            }
            else
            {
                viewHolder.ivAdd.setImageResource(R.drawable.ic_action_favorite);
                viewHolder.ivAdd.setEnabled(true);
            }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(onItemClickListener != null)onItemClickListener.onItemClick(show, position);
            }
        });

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utility.showToast(context, context.getString(R.string.show_will_be_downloaded, show.getTitle()));
                show.setShowAdded(true);
                viewHolder.ivAdd.setImageResource(R.drawable.ic_action_check);
                viewHolder.ivAdd.setEnabled(false);
                /*Intent intent = new Intent(context, ShowWorkerService.class);
                intent.setAction(ShowWorkerService.INTENT_ACTION_DOWNLOAD_SHOW);
                intent.putExtra(ShowWorkerService.INTENT_EXTRA_TVDB_ID, show.getTvdb_id());
                context.startService(intent);*/
                //TODO add to fav
            }
        });

        /*if(position == getItemCount() - 1)//last item
        {
            fragment.loadMore();
        }*/
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

    public void add(boolean clear, Show... s)
    {
        addAll(Arrays.asList(s), clear);
    }

    public void add(Show... s)
    {
        add(false, s);
    }

    public void addAll(List<Show> list, boolean clear)
    {
        if (clear)shows.clear();
        shows.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNextEpisode;
        public NetworkImageViewPlus ivPoster;
        ImageView ivAdd;
		TextView tvWatchedPercent, tvFavorite/*, tvTitle*/;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvNextEpisode = (TextView) itemView.findViewById(R.id.tvNextEpisode);
            //tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivPoster = (NetworkImageViewPlus) itemView.findViewById(R.id.imgSeriesImage);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
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
            /*int margin =  view.getContext().getResources().getDimensionPixelOffset(R.dimen.dp5);
            int columnCount = view.getContext().getResources().getInteger(R.integer.main_column_num);
            int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewPosition();
            int remainder = position % columnCount;
            if (position % columnCount == 0)
            {
                outRect.set(margin * 2, position < columnCount ? margin * 2 : 0, margin, margin);
            }
            else if (position % columnCount == columnCount - 1)
            {
                outRect.set(margin, position < columnCount ? margin * 2 : 0, margin * 2, margin);
            }
            else
            {
                outRect.set(0, position < columnCount ? margin * 2 : 0, 0, margin);
            }*/
        }
    }

}
