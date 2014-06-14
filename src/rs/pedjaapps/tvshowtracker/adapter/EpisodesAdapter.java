package rs.pedjaapps.tvshowtracker.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Season;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.MyTimer;
import rs.pedjaapps.tvshowtracker.utils.Utility;

import android.content.*;
import android.view.*;
import android.widget.*;

public final class EpisodesAdapter extends ArrayAdapter<Season>
{

    LayoutInflater inflater;
    boolean checkChangeListenerEnabled = true;

    public EpisodesAdapter(final Context context)
    {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        final Season season = getItem(position);
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.layout_season, parent ,false);
            holder = new ViewHolder();
            holder.tvSeason = (TextView) convertView.findViewById(R.id.tvSeason);
            holder.tvMarkAll = (TextView) convertView.findViewById(R.id.tvMarkAll);
            holder.llEpisodes = (LinearLayout)convertView.findViewById(R.id.llEpisodes);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSeason.setText(getContext().getString(R.string.season, season.getSeason()));
        holder.tvMarkAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for(Episode e : season.getEpisodes())
                {
                    e.setWatched(true);
                }
                MainApp.getInstance().getDaoSession().getEpisodeDao().insertOrReplaceInTx(season.getEpisodes());
                notifyDataSetChanged();
            }
        });
        //holder.llEpisodes.removeAllViews();
        MyTimer timer = new MyTimer();
        int index = 0;
        for(final Episode e : season.getEpisodes())
        {
            View episodeLayout;
            final ViewHolder childHolder;
            if (holder.llEpisodes.getChildAt(index) == null)
            {
                episodeLayout = inflater.inflate(R.layout.episode_layout, null);
                childHolder = new ViewHolder();
                childHolder.tvTitle = (TextView)episodeLayout.findViewById(R.id.tvTitle);
                childHolder.cbWatched = (CheckBox)episodeLayout.findViewById(R.id.cbWatched);
                episodeLayout.setTag(R.id.childHolder, childHolder);
                holder.llEpisodes.addView(episodeLayout);
            }
            else
            {
                episodeLayout = holder.llEpisodes.getChildAt(index);
                episodeLayout.setVisibility(View.VISIBLE);
                childHolder = (ViewHolder) episodeLayout.getTag(R.id.childHolder);
            }
            childHolder.tvTitle.setText(e.getTitle() + " | S" + (e.getSeason() < 10 ? "0" : "") + e.getSeason() + "E" + (e.getSeason() < 10 ? "0" : "") + e.getEpisode());
            childHolder.cbWatched.setChecked(e.isWatched());
            childHolder.cbWatched.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    e.setWatched(childHolder.cbWatched.isChecked());
                    MainApp.getInstance().getDaoSession().getEpisodeDao().insertOrReplace(e);
                }
            });
            episodeLayout.setTag(R.id.episode, e);

            index++;
        }
        for(int i = index; i < holder.llEpisodes.getChildCount(); i++)
        {
            View v = holder.llEpisodes.getChildAt(i);
            if(v != null)v.setVisibility(View.GONE);
        }
        //holder.llEpisodes.removeViewAt();
        timer.log("EpisodesAdapter.getView(): season " + season.getSeason() + " loaded in");

        return convertView;
    }


    public static String[] episode(Episode e)
    {
        StringBuilder b = new StringBuilder();
        b.append("S");
        String s = "0";
        if (e.getSeason() <= 10)
        {
            b.append("0").append(e.getSeason());
        }
        else
        {
            b.append(e.getSeason());
        }
        b.append("E");
        if (e.getEpisode() <= 10)
        {
            b.append("0").append(e.getEpisode());
        }
        else
        {
            b.append(e.getEpisode());
        }
        Date firstAired;
        try//TODO remove catch and handle first_aired null value
        {
            firstAired = new Date(e.getFirst_aired());
            if ((new Date().getTime() / (1000 * 60 * 60 * 24)) == (firstAired.getTime() / (1000 * 60 * 60 * 24)))
            {
                b.append(" | Airs today");
            }
            else if (new Date().before(firstAired))
            {
                b.append(" | Airs ").append(Constants.df2.format(firstAired));
                s = "1";
            }
            else if (new Date().after(firstAired))
            {
                b.append(" | Aired ").append(Constants.df2.format(firstAired));
            }
        }
        catch (Exception e1)
        {
            b.append(" | TBD");
            s = "1";
        }

        return new String[]{b.toString(), s};
    }

    class ViewHolder
    {
        TextView tvSeason, tvTitle, tvMarkAll;
        LinearLayout llEpisodes;
        CheckBox cbWatched;
    }
}
