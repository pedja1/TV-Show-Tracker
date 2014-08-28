package rs.pedjaapps.tvshowtracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PoppyViewHelper;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.pedjaapps.tvshowtracker.MainApp;
import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.adapter.ShowsAdapter;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Comparators;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.PrefsManager;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public abstract class ShowGridFragment extends Fragment
{
	ShowsAdapter adapter;
    GridView list;
    ProgressBar pbLoading;
    TextView tvNoShows;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_layout_show_grid, container, false);
		pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvNoShows = (TextView) view.findViewById(R.id.tvNoShows);
        setNoImageText();
		
		list = (GridView) view.findViewById(R.id.lvShows);

        
        adapter = new ShowsAdapter(getActivity());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					startActivity(new Intent(getActivity(),
											 ShowDetailsActivity.class).putExtra(ShowDetailsActivity.EXTRA_TVDB_ID,
																				 adapter.getItem(arg2).getTvdb_id()));
				}
			});
        
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		refreshShows();
	}
	
	private void setNoImageText()
    {
        tvNoShows.setText(noShowsString(), TextView.BufferType.SPANNABLE);
    }

    public void refreshShows()
    {
        new LoadShows().execute();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Restore the previously serialized current dropdown position.
        list.onRestoreInstanceState(savedInstanceState.getParcelable("list_position"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // Serialize the current dropdown position.
        Parcelable state = list.onSaveInstanceState();
        outState.putParcelable("list_position", state);
    }
	
	public class LoadShows extends AsyncTask<String, Void, List<Show>>
    {
        @Override
        protected List<Show> doInBackground(String... args)
        {
            long startTime = System.currentTimeMillis();
            List<Show> entry = getShows();
            Log.d(Constants.LOG_TAG,
				  "MainActivity.java > LoadShows > doInBackground: "
				  + (System.currentTimeMillis() - startTime) + "ms"
				  );
            return entry;
        }

        @Override
        protected void onPreExecute()
        {
            if(adapter.isEmpty())
            {
                pbLoading.setVisibility(View.VISIBLE);
                //tvNoShows.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(List<Show> result)
        {
            adapter.clear();
            for (Show s : result)
            {
                adapter.add(s);
            }
            adapter.notifyDataSetChanged();
            if(adapter.isEmpty())
            {
                tvNoShows.setVisibility(View.VISIBLE);
            }
            else
            {
                tvNoShows.setVisibility(View.GONE);
            }
            pbLoading.setVisibility(View.GONE);
            onShowsLoaded(result);
        }
    }
	
	protected abstract List<Show> getShows();
	protected abstract void onShowsLoaded(List<Show> shows);
	protected abstract CharSequence noShowsString();
}
