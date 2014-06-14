package rs.pedjaapps.tvshowtracker.adapter;


import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Actor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class ActorsAdapter extends ArrayAdapter<Actor>
{

    private final int itemLayoutResource;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public ActorsAdapter(final Context context, final int itemLayoutResource)
    {
        super(context, 0);
        this.itemLayoutResource = itemLayoutResource;
        options = new DisplayImageOptions.Builder().cloneFrom(MainApp.getInstance().displayImageOptions)
                .showImageForEmptyUri(R.drawable.noimage_banner)
                .showImageOnFail(R.drawable.noimage_banner)
                .build();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Actor actor = getItem(position);
        imageLoader.displayImage(actor.getImage(), viewHolder.ivActorPhoto, options);
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

            viewHolder.ivActorPhoto = (ImageView) workingView.findViewById(R.id.ivActorPhoto);
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
        ImageView ivActorPhoto;
        TextView tvName, tvRole;
    }

}
