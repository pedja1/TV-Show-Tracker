package rs.pedjaapps.tvshowtracker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Actor;

public final class ActorsAdapter extends ArrayAdapter<Actor>
{

    private final int itemLayoutResource;

    public ActorsAdapter(final Context context, final int itemLayoutResource)
    {
        super(context, 0);
        this.itemLayoutResource = itemLayoutResource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Actor actor = getItem(position);
        Ion.with(viewHolder.ivActorPhoto)
                .placeholder(R.drawable.noimage_banner)
                .error(R.drawable.noimage_banner)
                .load(actor.getImage());
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
