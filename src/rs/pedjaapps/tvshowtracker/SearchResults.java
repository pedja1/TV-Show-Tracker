package rs.pedjaapps.tvshowtracker;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import rs.pedjaapps.tvshowtracker.adapter.SearchAdapter;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.network.JSONUtility;
import rs.pedjaapps.tvshowtracker.utils.Utility;

public class SearchResults extends BaseActivity
{
    ListView searchListView;
    SearchAdapter searchAdapter;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_result);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        searchListView = (ListView) findViewById(R.id.list);
        View emptyView = getLayoutInflater().inflate(R.layout.search_results_empty_view, null);
        searchListView.setEmptyView(emptyView);

        searchAdapter = new SearchAdapter(this, R.layout.search_row);


        searchListView.setAdapter(searchAdapter);

        handleIntent(getIntent());

        searchListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                long tvdbId =  searchAdapter.getItem(position).getTvdb_id();
                /*ShowDao showDao = MainApp.getInstance().getDaoSession().getShowDao();
                QueryBuilder<Show> queryBuilder = showDao.queryBuilder();
                queryBuilder.where(ShowDao.Properties.Tvdb_id.eq(tvdbId));
                List<Show> show = queryBuilder.build().list();
                if (show.isEmpty())
                    new DownloadShow().execute(tvdbId + "");
                else
                    Toast.makeText(SearchResults.this, getString(R.string.show_exists), Toast.LENGTH_LONG).show();
					*/
				startActivity(new Intent(SearchResults.this, ShowDetailsActivity.class).putExtra(ShowDetailsActivity.EXTRA_TVDB_ID, tvdbId));
            }

        });
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        //SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //suggestions.saveRecentQuery(query, null);
            getSupportActionBar().setTitle(getString(R.string.searching, query));
            if (Utility.isNetworkAvailable(this))
                new ATGetSearchResults().execute(query);
            else
            {
                Toast.makeText(this, getString(R.string.no_net), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public class ATGetSearchResults extends AsyncTask<String, Void, List<Show>>
    {
        @Override
        protected List<Show> doInBackground(String... args)
        {
            return JSONUtility.parseSearchResults(args[0]);
        }

        @Override
        protected void onPreExecute()
        {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(List<Show> result)
        {
            searchAdapter.clear();
            if (result != null)
            {
                for (Show show : result)
                {
                    searchAdapter.add(show);
                }
            }
            searchAdapter.notifyDataSetChanged();
            setProgressBarIndeterminateVisibility(false);
        }
    }


}
