package rs.pedjaapps.tvshowtracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.ShowDetailsActivity;
import rs.pedjaapps.tvshowtracker.adapter.ShowsAdapter;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.widget.StateSaveRecyclerView;

public abstract class ShowGridFragment extends Fragment
{
	ShowsAdapter adapter;
    StateSaveRecyclerView list;
    ProgressBar pbLoading;
    TextView tvNoShows;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_layout_show_grid, container, false);
		pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvNoShows = (TextView) view.findViewById(R.id.tvNoShows);
        setNoImageText();
		
		list = (StateSaveRecyclerView) view.findViewById(R.id.lvShows);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.main_column_num));
        list.setLayoutManager(glm);

        
        adapter = new ShowsAdapter(getActivity(), new ArrayList<Show>());
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new ShowsAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(Show show, int position)
				{
					startActivity(new Intent(getActivity(),
											 ShowDetailsActivity.class).putExtra(ShowDetailsActivity.EXTRA_TVDB_ID,
																				 show.getTvdb_id()));
				}
			});
        
		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		refreshShows();
        // Restore the previously serialized current dropdown position.
        if(savedInstanceState != null)
        {
            list.post(new Runnable()
            {
                @Override
                public void run()
                {
                    list.onRestoreInstanceState(savedInstanceState.getParcelable("list_position"));
                }
            });
        }
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
			System.out.println(result);
			pbLoading.setVisibility(View.GONE);
			if(result == null || result.isEmpty())
            {
                tvNoShows.setVisibility(View.VISIBLE);
				tvNoShows.setText(noShowsString());
            }
            else
            {
                tvNoShows.setVisibility(View.GONE);
				adapter.clear();
				for (Show s : result)
				{
					adapter.add(s);
				}
				adapter.notifyDataSetChanged();
            }
            
            onShowsLoaded(result);
        }
    }
	
	protected void onShowsLoaded(List<Show> shows)
	{
		//do nothing here
	}
	
	protected abstract List<Show> getShows();
	protected abstract CharSequence noShowsString();
}
