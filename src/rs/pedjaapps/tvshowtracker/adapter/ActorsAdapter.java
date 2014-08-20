package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;

public final class ActorsAdapter extends ArrayAdapter<Actor>
{

    private final int itemLayoutResource;
    SimpleImageLoader mImageFetcher;
    public static final float IMAGE_RATIO = 0.7f;

    public ActorsAdapter(final Context context, final int itemLayoutResource)
    {
        super(context, 0);
        this.itemLayoutResource = itemLayoutResource;
        mImageFetcher = new SimpleImageLoader(getContext().getApplicationContext(), R.drawable.noimage_poster_actor, MainApp.getInstance().cacheParams);
        mImageFetcher.setMaxImageSize((int) ((DisplayManager.screenWidth / 5) * IMAGE_RATIO));
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Actor actor = getItem(position);
        viewHolder.ivActorPhoto.setDefaultImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivActorPhoto.setErrorImageResId(R.drawable.noimage_poster_actor);
        viewHolder.ivActorPhoto.setImageUrl(actor.getImage(), mImageFetcher);
        viewHolder.tvName.setText(actor.getName());
        viewHolder.tvRole.setText(actor.getCharacter());
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

            viewHolder.ivActorPhoto = (NetworkImageViewPlus) workingView.findViewById(R.id.ivActorPhoto);
            viewHolder.tvName = (TextView) workingView.findViewById(R.id.tvName);
            viewHolder.tvRole = (TextView) workingView.findViewById(R.id.tvRole);
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
        NetworkImageViewPlus ivActorPhoto;
        TextView tvName, tvRole;
    }

}
