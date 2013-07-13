package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.provider.*;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

import rs.pedjaapps.tvshowtracker.adapter.SearchAdapter;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.ActorsList;
import rs.pedjaapps.tvshowtracker.model.EpisodeItem;
import rs.pedjaapps.tvshowtracker.model.SearchResults;
import rs.pedjaapps.tvshowtracker.model.Series;
import rs.pedjaapps.tvshowtracker.model.SeriesData;
import rs.pedjaapps.tvshowtracker.utils.DatabaseHandler;
import rs.pedjaapps.tvshowtracker.utils.Internet;
import rs.pedjaapps.tvshowtracker.utils.SuggestionProvider;
import rs.pedjaapps.tvshowtracker.utils.Tools;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.google.gson.Gson;


public class SearchResultsActivity extends SherlockActivity {

	
	ListView searchListView;
	SearchAdapter searchAdapter;
	DatabaseHandler db;
	String extStorage = Environment.getExternalStorageDirectory().toString();
	String profile;
	SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.search_result);
		db = new DatabaseHandler(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		profile = prefs.getString("profile", "Default");
		
		searchListView = (ListView) findViewById(R.id.list);
		
		
		searchAdapter = new SearchAdapter(this, R.layout.search_row);

		searchListView.setAdapter(searchAdapter);

		handleIntent(getIntent());
		
		searchListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(!db.showExists(searchAdapter.getItem(position).getSeriesName(), profile))
					new DownloadShowInfo().execute(searchAdapter.getItem(position).getSeriesId()+"", searchAdapter.getItem(position).getLanguage());
				else
					Toast.makeText(SearchResultsActivity.this, "Show Exists!\nSelect Another", Toast.LENGTH_LONG).show();
			}
			
		});
		


		
}
	

	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		    String query = intent.getStringExtra(SearchManager.QUERY);
		    suggestions.saveRecentQuery(query, null);
			getSupportActionBar().setTitle("Searching for "+query);
			if(Tools.isNetworkAvailable(this))
				new TitleSearchParser().execute(query.replaceAll(" ", "%20"));
			else
			{
				Toast.makeText(this, "No Internet Connection!\nPlease connect to internet and try again!", Toast.LENGTH_LONG).show();
				finish();
			}
		    }
		}
	
	public class DownloadShowInfo extends AsyncTask<String, Integer[], String>
	{
		ProgressDialog pd;
		@Override
		protected String doInBackground(String... args)
		{
			
			/*publishProgress(new Integer[] {0,0});
			
			
			if(!new File(extStorage+"/TVST/actors").exists()){
				new File(extStorage+"/TVST/actors").mkdirs();
			}
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+args[0]+"/all/"+args[1]+".xml"); 
			
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			NodeList nl = doc.getElementsByTagName("Series");
			Element e = (Element) nl.item(0);
			String date = Constants.df.format(new Date());
			String seriesId = parser.getValue(e, "id");
			db.addShow(new Series(parser.getValue(e, "SeriesName"), parser.getValue(e, "FirstAired"),
					parser.getValue(e, "IMDB_ID"), parser.getValue(e, "Overview"),
					Tools.parseRating(parser.getValue(e, "Rating")), Integer.parseInt(parser.getValue(e, "id")),
					parser.getValue(e, "Language"), 
					Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "banner"), extStorage+"/TVST"+parser.getValue(e, "banner").substring(parser.getValue(e, "banner").lastIndexOf("/")), true), 
					Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "fanart"), extStorage+"/TVST"+parser.getValue(e, "fanart").substring(parser.getValue(e, "fanart").lastIndexOf("/")), true), 
					parser.getValue(e, "Network"), Tools.parseInt(parser.getValue(e, "Runtime")), 
					parser.getValue(e, "Status"), false, false, date, parser.getValue(e, "Actors")), profile);
			
			nl = doc.getElementsByTagName("Episode");
			
			for(int i =0; i < nl.getLength(); i++){
				e = (Element) nl.item(i);
				if(!parser.getValue(e, "SeasonNumber").equals("0") && !db.episodeExists(seriesId, parser.getValue(e, "id"), profile)){
				db.addEpisode(new EpisodeItem(parser.getValue(e, "EpisodeName"),
						Tools.parseInt(parser.getValue(e, "EpisodeNumber")), 
						Tools.parseInt(parser.getValue(e, "SeasonNumber")),
						parser.getValue(e, "FirstAired"), parser.getValue(e, "IMDB_ID"), 
						parser.getValue(e, "Overview"), Tools.parseRating(parser.getValue(e, "Rating")),
						false, Tools.parseInt(parser.getValue(e, "id")), profile), seriesId);
				}
				publishProgress(new Integer[]{1,i,nl.getLength()});
			}
			xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+args[0]+"/actors.xml"); 
			doc = parser.getDomElement(xml);
			nl = doc.getElementsByTagName("Actor");
			for(int i =0; i < nl.getLength(); i++){
				e = (Element) nl.item(i);
				String image = "";
				try{
					image = Tools.DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "Image"), extStorage+"/TVST/actors"+parser.getValue(e, "Image").substring(parser.getValue(e, "Image").lastIndexOf("/")), true);
				}
				catch(Exception ex){
				}
				if(!db.episodeExists(seriesId, parser.getValue(e, "id"), profile))
				{
				db.addActor(new Actor(parser.getValue(e, "id"), parser.getValue(e, "Name"), parser.getValue(e, "Role"), 
						image, profile), seriesId);
				}
				publishProgress(new Integer[]{2,i,nl.getLength()});
			}*/
            publishProgress(new Integer[] {0,0});
            Gson gson = new Gson();
            String seriesJson = Internet.httpGet("http://pedjaapps.in.rs/tvst/api/series.php?seriesid="+args[0]);
            SeriesData data = gson.fromJson(seriesJson, SeriesData.class);
            Series s = data.getData().getSeries();
            db.addShow(s, profile);
            List<EpisodeItem> episodes = data.getData().getEpisodes();
            for(int i = 0; i < episodes.size(); i++)
            {
                EpisodeItem e = episodes.get(i);
                e.setProfile(profile);
                if(e.getEpisode()!=0 && !db.episodeExists(s.getSeriesId()+"", e.getEpisodeId()+"", profile)){
                    db.addEpisode(e, s.getSeriesId()+"");
                }
                publishProgress(new Integer[]{1,i,episodes.size()});
            }
            String actorsJson = Internet.httpGet("http://pedjaapps.in.rs/tvst/api/actors.php?seriesid="+args[0]);
            ActorsList data2 = gson.fromJson(actorsJson, ActorsList.class);
            List<Actor> actors = data2.getActorsObject().getActorsList();
            for(int i = 0; i < actors.size(); i++)
            {
                Actor a = actors.get(i);
                a.setProfile(profile);
                if(!db.actorExists(s.getSeriesId()+"", a.getActorId(), profile)){
                    db.addActor(a, s.getSeriesId()+"");
                }
                publishProgress(new Integer[]{2,i,actors.size()});
            }



			return "";
			
		}
		
		@Override
		protected void onProgressUpdate(Integer[]... progress){
			if((progress[0])[0]==0)
				pd.setMessage("Downloading Show Information");
			else if((progress[0])[0]==1)
			{
				pd.setMessage("Downloading Episodes "+(progress[0])[1]+"/"+(progress[0])[2]);
			}
			else{
				pd.setMessage("Downloading Actors "+(progress[0])[1]+"/"+(progress[0])[2]);
			}
		}
		
		@Override
		protected void onPreExecute(){
			Tools.setKeepScreenOn(SearchResultsActivity.this, true);
			pd = new ProgressDialog(SearchResultsActivity.this);
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
			finish();
			Tools.setRefresh(true);
			Tools.setKeepScreenOn(SearchResultsActivity.this, false);
		}
	}	
	
	public class TitleSearchParser extends AsyncTask<String, Void, List<SearchResults.SearchResult>>
	{

		@Override
		protected List<SearchResults.SearchResult> doInBackground(String... args)
		{
			List<SearchResults.SearchResult> entry;

            String json = Internet.httpGet("http://pedjaapps.in.rs/tvst/api/search.php?seriesname="+args[0]);
            Gson gson = new Gson();
            SearchResults obj = gson.fromJson(json, SearchResults.class);
            entry = obj.getData().getSeries();
			
			return entry;
		}

		@Override
		protected void onPreExecute(){
			setSupportProgressBarIndeterminateVisibility(true);
			 }
		
		@Override
		protected void onPostExecute(List<SearchResults.SearchResult> result)
		{
            setSupportProgressBarIndeterminateVisibility(false);
			searchAdapter.clear();
			for (SearchResults.SearchResult entry : result) {
				searchAdapter.add(entry);
			}
			searchAdapter.notifyDataSetChanged();
		}
	}	

	
 
}
