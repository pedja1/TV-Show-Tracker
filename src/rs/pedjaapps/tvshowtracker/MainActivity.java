package rs.pedjaapps.tvshowtracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends SherlockActivity {

	
	ShowsAdapter adapter;
	GridView list;
	RelativeLayout emptyView;
	ProgressBar loading;
	TextView listEmpty;
	DatabaseHandler db;
	SearchView searchView;
	ActionMode mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DatabaseHandler(this);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Add new Movie");
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setQueryRefinementEnabled(true);
		
		emptyView = (RelativeLayout)findViewById(R.id.emptyView);
		loading = (ProgressBar)findViewById(R.id.pgrSearch);
		listEmpty = (TextView)findViewById(R.id.txtMessage);
		adapter = new ShowsAdapter(this, R.layout.shows_list_row);
		list = (GridView)findViewById(R.id.list);
		list.setAdapter(adapter);
		
		
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(MainActivity.this, DetailsActivity.class).putExtra("seriesId", adapter.getItem(arg2).getSeriesId()));
				System.out.println(adapter.getItem(arg2).getSeriesId());
			}
			
		});
		
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (mode != null)
                {
                        mode.finish();
                }
                mode = startActionMode(new ListActionMode(position));
				return true;
			}
		});
 			
	}

	@Override
	protected void onResume(){
		new LoadShows().execute();
		super.onResume();
	}
	
	private void setUI(){
		if(adapter.isEmpty()){
			listEmpty.setVisibility(View.VISIBLE);
			loading.setVisibility(View.INVISIBLE);
		}
		else{
			emptyView.setVisibility(View.GONE);
		}
		
	}
	
	private List<Show> getShows(){
		List<Show> shows = new ArrayList<Show>();
		List<Show> dbShows = db.getAllShows();
		for(Show s : dbShows){
			shows.add(new Show(s.getBanner(), upcomingEpisode(s.getSeriesId()+"", s.getStatus()), watchedPercent(s.getSeriesId()+""), s.getSeriesId()));
		}
		return shows;
	}
	
	public class LoadShows extends AsyncTask<String, Void, List<Show>>
	{

		@Override
		protected List<Show> doInBackground(String... args)
		{
			List<Show> entry = new ArrayList<Show>();
			for(Show s : getShows()){
				entry.add(s);
			}
			
			
			return entry;
		}

		@Override
		protected void onPreExecute(){

			loading.setVisibility(View.VISIBLE);
			 }
		
		@Override
		protected void onPostExecute(List<Show> result)
		{
			adapter.clear();
			for(Show s : result){
				adapter.add(s);
			}
			adapter.notifyDataSetChanged();
			setUI();
		}
	}	
	
	private String upcomingEpisode(String seriesId, String status){
		StringBuilder b = new StringBuilder();
		if(status != null && status.equals("Ended")){
			b.append("THIS SHOW HAS ENDED");
		}
		else
		{
		List<EpisodeItem> episodeItems = db.getAllEpisodes(seriesId);
		for(EpisodeItem e : episodeItems){
			//date is in format "yyyy-MM-dd"
			try{
				Date firstAired = Constants.df.parse(e.getFirstAired());
				if(new Date().before(firstAired) || (new Date().getTime() / (1000*60*60*24)) == (firstAired.getTime()/ (1000*60*60*24))){
					b.append("S");
					if(e.getSeason()<=10){
						b.append("0"+e.getSeason());
					}
					else{
						b.append(e.getSeason());
					}
					b.append("E");
					if(e.getEpisode()<=10){
						b.append("0"+e.getEpisode()+" "+e.getEpisodeName()+" - AIRS ");
					}
					else{
						b.append(e.getEpisode()+" "+e.getEpisodeName()+" - AIRS ");
					}
					long days = ((firstAired.getTime()-System.currentTimeMillis()) / (1000*60*60*24));
					if(days>0)
						b.append("in "+days+" DAYS");
					else
						b.append("TODAY");
					break;
				}
			}
			catch(Exception ex){
			}
		}
		
		if(b.length() < 1){
			b.append("NO INFORMATION ABOUT NEXT EPISODE");
		}
		}
		return b.toString();
	}
	
	private int watchedPercent(String seriesId){
		
		List<EpisodeItem> episodeItems = db.getAllEpisodes(seriesId);
		int episodeCount = db.getEpisodesCount(seriesId);
		int watched = 0;
		for(EpisodeItem e : episodeItems){
			if(e.isWatched()){
				watched++;
			}
		}
		return (int)((double)watched/(double)episodeCount*100.0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Add")
		.setIcon(R.drawable.ic_action_search)
		.setActionView(searchView)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, 1, 1, "Agenda")
			.setIcon(R.drawable.ic_action_agenda)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0, 2, 2, "Preferences")
		.setIcon(R.drawable.ic_action_settings)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0, 3, 3, "Update All")
		.setIcon(R.drawable.ic_action_update)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 1:
				startActivity(new Intent(this, AgendaActivity.class));
			return true;
			case 2:
				startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public class ListActionMode implements ActionMode.Callback{
		int id;
		int selectedCount;
		int position;
		
		public ListActionMode(int position){
			this.position = position;
		}
		
		@Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MainActivity.this.mode = mode;
            /*mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");*/
			menu.add(0, 0, 0, "Delete")
				.setIcon(R.drawable.ic_action_delete)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            menu.add(0, 1, 1, "Update")
				.setIcon(R.drawable.ic_action_update)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			
			return true;
			
        }

		@Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			
			switch(item.getItemId()){
			case 0:
				db.deleteSeries(adapter.getItem(position).getSeriesId()+"");
				adapter.remove(adapter.getItem(position));
				adapter.notifyDataSetChanged();
				setUI();
				mode.finish();
				break;
			case 1:
				break;
		}
		
        return true;
		}

		

    }
	
}
