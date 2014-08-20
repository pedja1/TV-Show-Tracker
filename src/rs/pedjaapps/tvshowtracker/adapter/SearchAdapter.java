package rs.pedjaapps.tvshowtracker.adapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowOld;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.nostra13.universalimageloader.core.ImageLoader;

public final class SearchAdapter extends ArrayAdapter<Show>
{

    private final int itemLayoutResource;
    Context context;

    public SearchAdapter(final Context context, final int itemLayoutResource)
    {

        super(context, 0);
        this.itemLayoutResource = itemLayoutResource;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {

        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Show show = getItem(position);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
        viewHolder.tvTitleYear.setText(show.getTitle() + " (" + show.getYear() + ")");
        viewHolder.tvNetwork.setText(show.getNetwork());
        viewHolder.tvAirDay.setText(show.getAir_day() + ", " + show.getAir_time());
        viewHolder.tvOverview.setText(show.getOverview());
        ImageLoader.getInstance().displayImage(show.getBanner(), viewHolder.ivBanner);
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

            viewHolder.tvTitleYear = (TextView) workingView.findViewById(R.id.tvTitleYear);
            viewHolder.tvNetwork = (TextView) workingView.findViewById(R.id.tvNetwork);
            viewHolder.tvOverview = (TextView) workingView.findViewById(R.id.tvOverview);
            //viewHolder.countryView = (TextView) workingView.findViewById(R.id.country);
            viewHolder.tvAirDay = (TextView) workingView.findViewById(R.id.tvAirTime);
            viewHolder.ivBanner = (ImageView) workingView.findViewById(R.id.ivBanner);

            workingView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    private class ViewHolder
    {
        TextView tvTitleYear, tvNetwork, tvAirDay, tvOverview;
        ImageView ivBanner;
    }


}
