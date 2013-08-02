package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import rs.pedjaapps.tvshowtracker.adapter.*;
import rs.pedjaapps.tvshowtracker.model.*;
import rs.pedjaapps.tvshowtracker.utils.*;
public class AgendaActivity extends Activity {

	AgendaAdapter adapter;
	ListView list;
	DatabaseHandler db;
	String profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_agenda);
		
		db = new DatabaseHandler(this);
	    list = (ListView)findViewById(R.id.list);
		adapter = new AgendaAdapter(this);
		profile = getIntent().getStringExtra("profile");
		for(Agenda a : getItems()){
			adapter.add(a);
		}
		list.setAdapter(adapter);
	}

	private List<Agenda> getItems(){
		List<Agenda> a = new ArrayList<Agenda>();
		List<Show> shows = db.getAllShows("", profile);
		for(Show s : shows){
			List<EpisodeItem> episodes = db.getAllEpisodes(s.getSeriesId()+"", profile);
			for(EpisodeItem e : episodes){
				if(e.getSeason() != 0){
					try{
						Date firstAired = Constants.df.parse(e.getFirstAired());
						if(new Date().before(firstAired) || (new Date().getTime() / (1000*60*60*24)) == (firstAired.getTime()/ (1000*60*60*24)))
						{
							a.add(new AgendaItem(e.getEpisodeName(), s.getBanner(), EpisodesAdapter.episode(e)[0]));
						}
					}
					catch(Exception ex){
						
					}
				}
			}
		}
		
		
		return a;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
