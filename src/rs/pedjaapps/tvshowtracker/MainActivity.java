package rs.pedjaapps.tvshowtracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {

	
	ShowsAdapter adapter;
	GridView list;
	RelativeLayout emptyView;
	ProgressBar loading;
	TextView listEmpty;
	DatabaseHandler db;
	SearchView searchView;
	ActionMode mode;
	String extStorage = Environment.getExternalStorageDirectory().toString();
	SharedPreferences prefs;
	String profile;
	String sort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
				startActivity(new Intent(MainActivity.this, DetailsActivity.class).putExtra("seriesId", adapter.getItem(arg2).getSeriesId()).putExtra("profile", profile));
				
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
		profile = prefs.getString("profile", "Default");
		sort = prefs.getString("sort", "default");
		StringBuilder b = new StringBuilder();
		b.append(profile);
		if(sort.equals("name")){
			b.append(" | Sorted by Name");
		}
		else if(sort.equals("next")){
			b.append(" | Sorted by Next Episode");
		}
		else if(sort.equals("unwatched")){
			b.append(" | Sorted by Unwatched Episodes");
		}
		getSupportActionBar().setSubtitle(b.toString());
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
		List<Show> dbShows = db.getAllShows(prefs.getString("filter", "all"), profile);
		for(Show s : dbShows){
			String[] ue = upcomingEpisode(s.getSeriesId()+"", s.getStatus());
			shows.add(new Show(s.getSeriesName(), s.getBanner(), ue[0], watchedPercent(s.getSeriesId()+""), s.getSeriesId(), Integer.parseInt(ue[1])));
		}
		
		if(sort.equals("name")){
			Collections.sort(shows, new SortByName());
		}
		else if(sort.equals("next")){
			Collections.sort(shows, new SortByNextEpisode());
		}
		else if(sort.equals("unwatched")){
			Collections.sort(shows, new SortByUnwatched());
		}
		
		return shows;
	}
	
	static class SortByName implements Comparator<Show>
	{
		@Override
		public int compare(Show s1, Show s2)
		{
		    String sub1 = s1.getSeriesName();
		    String sub2 = s2.getSeriesName();
		    return sub1.compareTo(sub2);
		} 

	}
	static class SortByNextEpisode implements Comparator<Show>
	{
		@Override
		public int compare(Show p1, Show p2)
		{
	        if (p1.getNextEpisodeDays() < p2.getNextEpisodeDays()) return 1;
	        if (p1.getNextEpisodeDays() > p2.getNextEpisodeDays()) return -1;
	        return 0;
	    }   

	}
	static class SortByUnwatched implements Comparator<Show>
	{
		@Override
		public int compare(Show p1, Show p2)
		{
	        if (p1.getPrgWatched() < p2.getPrgWatched()) return -1;
	        if (p1.getPrgWatched() > p2.getPrgWatched()) return 1;
	        return 0;
	    }   

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
	
	private String[] upcomingEpisode(String seriesId, String status){
		StringBuilder b = new StringBuilder();
		long days = -1;
		if(status != null && status.equals("Ended")){
			b.append("THIS SHOW HAS ENDED");
		}
		else
		{
		List<EpisodeItem> episodeItems = db.getAllEpisodes(seriesId, profile);
		for(EpisodeItem e : episodeItems){
			//date is in format "yyyy-MM-dd"
			try{
				Date firstAired = Constants.df.parse(e.getFirstAired());
				if(new Date().before(firstAired) || (new Date().getTime() / (1000*60*60*24)) == (firstAired.getTime()/ (1000*60*60*24))){
					b.append("S");
					if(e.getSeason()<=10){
						b.append("0").append(e.getSeason());
					}
					else{
						b.append(e.getSeason());
					}
					b.append("E");
					if(e.getEpisode()<=10){
						b.append("0").append(e.getEpisode()).append(" ").append(e.getEpisodeName()).append(" - AIRS ");
					}
					else{
						b.append(e.getEpisode()).append(" ").append(e.getEpisodeName()).append(" - AIRS ");
					}
					days = ((firstAired.getTime()-System.currentTimeMillis()) / (1000*60*60*24));
					if(days>0)
						b.append("in ").append(days).append(" DAYS");
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
		return new String[] {b.toString(), days+""};
	}
	
	private int watchedPercent(String seriesId){
		
		List<EpisodeItem> episodeItems = db.getAllEpisodes(seriesId, profile);
		int episodeCount = db.getEpisodesCount(seriesId, profile);
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
				startActivity(new Intent(this, AgendaActivity.class).putExtra("profile", profile));
				return true;
			case 2:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case 3:
				if(Tools.isNetworkAvailable(this)){
				new UpdateShow().execute(db.getAllShows("", profile));
				}
				else{
					Toast.makeText(MainActivity.this, "No Internet Connection!\nPlease connect to internet and try again!", Toast.LENGTH_LONG).show();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public class ListActionMode implements ActionMode.Callback{
		int id;
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
				db.deleteSeries(adapter.getItem(position).getSeriesId()+"", profile);
				adapter.remove(adapter.getItem(position));
				adapter.notifyDataSetChanged();
				setUI();
				mode.finish();
				break;
			case 1:
				if(Tools.isNetworkAvailable(MainActivity.this)){
				List<Show> list = new ArrayList<Show>();
				list.add(db.getShow(adapter.getItem(position).getSeriesId()+"", profile));
				new UpdateShow().execute(list);
				}
				else{
					Toast.makeText(MainActivity.this, "No Internet Connection!\nPlease connect to internet and try again!", Toast.LENGTH_LONG).show();
				}
				mode.finish();
				break;
		}
		
        return true;
		}

		

    }
	public class UpdateShow extends AsyncTask<List<Show>, String[], String>
	{
		
		ProgressDialog pd;
		@Override
		protected String doInBackground(List<Show>... args)
		{
			for(int n = 0; n < args[0].size(); n++){
			publishProgress(new String[] {n+1+"",args[0].get(n).getSeriesName(), args[0].size()+""});
			
			Show s = args[0].get(n);
			if(!new File(extStorage+"/TVST/actors").exists()){
				new File(extStorage+"/TVST/actors").mkdirs();
			}
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+s.getSeriesId()+"/all/"+s.getLanguage()+".xml"); 
			
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			NodeList nl = doc.getElementsByTagName("Series");
			Element e = (Element) nl.item(0);
			String date = Constants.df.format(new Date());
			String seriesId = parser.getValue(e, "id");
			
				db.updateShow(new Show(parser.getValue(e, "SeriesName"), parser.getValue(e, "FirstAired"), 
						parser.getValue(e, "IMDB_ID"), parser.getValue(e, "Overview"),
						Tools.parseRating(parser.getValue(e, "Rating")), Integer.parseInt(parser.getValue(e, "id")),
						parser.getValue(e, "Language"), 
						Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "banner"), extStorage+"/TVST"+parser.getValue(e, "banner").substring(parser.getValue(e, "banner").lastIndexOf("/"))), 
						Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "fanart"), extStorage+"/TVST"+parser.getValue(e, "fanart").substring(parser.getValue(e, "fanart").lastIndexOf("/"))), 
						parser.getValue(e, "Network"), Tools.parseInt(parser.getValue(e, "Runtime")), 
						parser.getValue(e, "Status"), false, false, date, parser.getValue(e, "Actors")), seriesId, profile);
			
			nl = doc.getElementsByTagName("Episode");
			
			for(int i =0; i < nl.getLength(); i++){
				e = (Element) nl.item(i);
				if(!parser.getValue(e, "SeasonNumber").equals("0")){
				if(!db.episodeExists(seriesId, parser.getValue(e, "id"), profile)){
				db.addEpisode(new EpisodeItem(parser.getValue(e, "EpisodeName"),
						Tools.parseInt(parser.getValue(e, "EpisodeNumber")), 
						Tools.parseInt(parser.getValue(e, "SeasonNumber")),
						parser.getValue(e, "FirstAired"), parser.getValue(e, "IMDB_ID"), 
						parser.getValue(e, "Overview"), Tools.parseRating(parser.getValue(e, "Rating")),
						false, Tools.parseInt(parser.getValue(e, "id")), profile), seriesId);
				}
				else{
					db.updateEpisode(new EpisodeItem(parser.getValue(e, "EpisodeName"),
							Tools.parseInt(parser.getValue(e, "EpisodeNumber")), 
							Tools.parseInt(parser.getValue(e, "SeasonNumber")),
							parser.getValue(e, "FirstAired"), parser.getValue(e, "IMDB_ID"), 
							parser.getValue(e, "Overview"), Tools.parseRating(parser.getValue(e, "Rating")),
							db.getEpisode(seriesId, parser.getValue(e, "id"), profile).isWatched(), Tools.parseInt(parser.getValue(e, "id")), profile), parser.getValue(e, "id"),seriesId);
				}
				}
			}
			xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+s.getSeriesId()+"/actors.xml"); 
			doc = parser.getDomElement(xml);
			nl = doc.getElementsByTagName("Actor");
			for(int i =0; i < nl.getLength(); i++){
				e = (Element) nl.item(i);
				if(!db.actorExists(seriesId, parser.getValue(e, "id"), profile)){
				
				db.addActor(new Actor(parser.getValue(e, "id"), parser.getValue(e, "Name"), parser.getValue(e, "Role"), 
						Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "Image"), extStorage+"/TVST/actors"+parser.getValue(e, "Image").substring(parser.getValue(e, "Image").lastIndexOf("/"))), profile), seriesId);
				}
				else{
					db.updateActor(new Actor(parser.getValue(e, "id"), parser.getValue(e, "Name"), parser.getValue(e, "Role"), 
							Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "Image"), extStorage+"/TVST/actors"+parser.getValue(e, "Image").substring(parser.getValue(e, "Image").lastIndexOf("/"))), profile), parser.getValue(e, "id"), seriesId);
				}
			}
			}
			return "";
			
		}
		
		@Override
		protected void onProgressUpdate(String[]... progress){
			
				pd.setMessage("Updating "+(progress[0])[1]+" - "+(progress[0])[0]+"/"+(progress[0])[2]);
			
		}
		
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(MainActivity.this);
			pd.setIndeterminate(true);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setTitle("Downloading Show Info");
			pd.show();
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			pd.dismiss();
			
		}
	}	
}
