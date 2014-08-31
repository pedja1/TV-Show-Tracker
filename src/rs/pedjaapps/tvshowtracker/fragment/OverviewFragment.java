package rs.pedjaapps.tvshowtracker.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import rs.pedjaapps.tvshowtracker.widget.NotifyingScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeItem;
import rs.pedjaapps.tvshowtracker.model.Genre;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.DisplayManager;

/**
 * Created by pedja on 1.6.14..
 */
public class OverviewFragment extends Fragment implements View.OnClickListener
{
    Show show;

    public static OverviewFragment newInstance()
    {
        OverviewFragment overviewFragment = new OverviewFragment();
        return overviewFragment;
    }

    private static void setProgress()
    {
        final List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();//db.getAllEpisodes(seriesId + "", profile);
        int episodeCount = 0;//db.getEpisodesCount(seriesId + "", profile);
        int watched = 0;
        for (EpisodeItem e : episodes)
        {
            if (e.isWatched())
            {
                watched++;
            }
        }
        //prgWatched.setProgress((int) ((double) watched/ (double) episodeCount * 100.0));
        //watchedText.setText(watched + "/" + episodeCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        show = ((ShowDetailsActivity)getActivity()).getShow();
        View view = inflater.inflate(R.layout.datails_overview, container, false);
        TextView tvOverview = (TextView)view.findViewById(R.id.tvOverview);
        tvOverview.setText(show.getOverview());
        TextView tvYear = (TextView)view.findViewById(R.id.tvYear);
        tvYear.setText(Html.fromHtml(getString(R.string.year, show.getYear())));
        TextView tvCountry = (TextView)view.findViewById(R.id.tvCountry);
        tvCountry.setText(Html.fromHtml(getString(R.string.country, show.getCountry())));
        TextView tvRuntime = (TextView)view.findViewById(R.id.tvRuntime);
        tvRuntime.setText(Html.fromHtml(getString(R.string.runtime, show.getRuntime())));
        TextView tvStatus = (TextView)view.findViewById(R.id.tvStatus);
        tvStatus.setText(Html.fromHtml(getString(R.string.status, show.getStatus())));
        TextView tvNetwork = (TextView)view.findViewById(R.id.tvNetwork);
        tvNetwork.setText(Html.fromHtml(getString(R.string.network, show.getNetwork())));
        TextView tvAirTime = (TextView)view.findViewById(R.id.tvAirTime);
        tvAirTime.setText(Html.fromHtml(getString(R.string.air_time, show.getAir_day() + show.getAir_time())));
        TextView tvCertification = (TextView)view.findViewById(R.id.tvCertification);
        tvCertification.setText(Html.fromHtml(getString(R.string.certification, show.getCertification())));
        TextView tvRating = (TextView)view.findViewById(R.id.tvRating);
        tvRating.setText(Html.fromHtml(getString(R.string.rating_, show.getRating() != null ? ((double)show.getRating() / 10.0) + "" : "")));
        TextView tvGenres = (TextView)view.findViewById(R.id.tvGenres);
        tvGenres.setText(generateGenres(show.getGenres()));
        TextView tvWatchedEpisodes = (TextView)view.findViewById(R.id.tvWatchedEpisodes);
        tvWatchedEpisodes.setText(Html.fromHtml(getString(R.string.watched_episodes, generateWatchedEpisodes(show.getEpisodes()))));
        final NetworkImageViewPlus ivShowPhoto = (NetworkImageViewPlus)view.findViewById(R.id.ivShowImage);

        SimpleImageLoader mImageFetcher = new SimpleImageLoader(getActivity().getApplicationContext(), R.drawable.noimage_poster_actor, MainApp.getInstance().cacheParams);
        mImageFetcher.setMaxImageSize((DisplayManager.screenWidth / 100 * 90));

        ivShowPhoto.setDefaultImageResId(R.drawable.noimage_fanart);
        ivShowPhoto.setErrorImageResId(R.drawable.noimage_fanart);
        ivShowPhoto.setImageUrl(show.getImage().getFanart(), mImageFetcher);
        
		TextView tvShowName = (TextView)view.findViewById(R.id.tvShowName);
        tvShowName.setText(show.getTitle());
        TextView tvShowShortInfo = (TextView)view.findViewById(R.id.tvShowShortInfo);
        tvShowShortInfo.setText(show.getAir_day() + " " + show.getAir_time() + " - " + show.getNetwork());
        ImageView ivImdb = (ImageView)view.findViewById(R.id.ivImdb);
        ivImdb.setOnClickListener(this);
        ImageView ivTrakt = (ImageView)view.findViewById(R.id.ivTrakt);
        ivTrakt.setOnClickListener(this);

        ((NotifyingScrollView)view).setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt)
            {
                final int headerHeight = ivShowPhoto.getHeight() - getActivity().getActionBar().getHeight();
                final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
                final int newAlpha = (int) (ratio * 255);
				((ShowDetailsActivity)getActivity()).setActionBarAlpha(newAlpha);
                ((ShowDetailsActivity)getActivity()).getActionBarBackgroundDrawable().setAlpha(newAlpha);
            }
        });

        return view;
    }

    private String generateWatchedEpisodes(List<Episode> episodes)
    {
        int count = episodes.size();
        int watched = 0;
        for(Episode e : episodes)
        {
            if(e.isWatched())watched++;
        }
        return watched + "/" + count;
    }

    private String generateGenres(List<Genre> genres)
    {
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        for(Genre genre : genres)
        {
            if(offset != 0)builder.append(", ");
            builder.append(genre.getName());
            offset++;
        }
        return builder.toString();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivImdb:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.IMDB_URL_PREFIX + show.getImdb_id()));
                startActivity(Intent.createChooser(intent, null));
                break;
            case R.id.ivTrakt:
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(show.getUrl()));
                startActivity(Intent.createChooser(intent, null));
                break;
        }
    }
}
