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
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
        ImageView ivShowPhoto = (ImageView)view.findViewById(R.id.ivShowImage);
        
		DisplayImageOptions options = new DisplayImageOptions.Builder().cloneFrom(MainApp.getInstance().displayImageOptions)
			.showImageForEmptyUri(R.drawable.noimage_fanart)
			.showImageOnFail(R.drawable.noimage_fanart)
			.showImageOnLoading(R.drawable.noimage_fanart).build();
		
		ImageLoader.getInstance().displayImage(show.getImage().getFanart(), ivShowPhoto);
        
		TextView tvShowName = (TextView)view.findViewById(R.id.tvShowName);
        tvShowName.setText(show.getTitle());
        TextView tvShowShortInfo = (TextView)view.findViewById(R.id.tvShowShortInfo);
        tvShowShortInfo.setText(show.getAir_day() + " " + show.getAir_time() + " - " + show.getNetwork());
        ImageView ivImdb = (ImageView)view.findViewById(R.id.ivImdb);
        ivImdb.setOnClickListener(this);
        ImageView ivTrakt = (ImageView)view.findViewById(R.id.ivTrakt);
        ivTrakt.setOnClickListener(this);
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
