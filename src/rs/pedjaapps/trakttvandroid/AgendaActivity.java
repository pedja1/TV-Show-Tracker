package rs.pedjaapps.trakttvandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rs.pedjaapps.trakttvandroid.adapter.AgendaAdapter;
import rs.pedjaapps.trakttvandroid.adapter.EpisodesAdapter;
import rs.pedjaapps.trakttvandroid.model.Agenda;
import rs.pedjaapps.trakttvandroid.model.AgendaItem;
import rs.pedjaapps.trakttvandroid.model.AgendaSection;
import rs.pedjaapps.trakttvandroid.model.EpisodeItem;
import rs.pedjaapps.trakttvandroid.model.Show;
import rs.pedjaapps.trakttvandroid.utils.Constants;
public class AgendaActivity extends BaseActivity 
{

	AgendaAdapter adapter;
	ListView list;
	String profile;
	CaldroidFragment caldroidFragment;
	List<Agenda> a;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_agenda);
		
			final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		////////////////////////////////////////////////////////////////////////
		// **** This is to show customized fragment. If you want customized
		// version, uncomment below line ****
		// caldroidFragment = new CaldroidSampleCustomFragment();

		// Setup arguments

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.FIT_ALL_MONTHS, false);

			// Uncomment this to customize startDayOfWeek
			 args.putInt("startDayOfWeek", 1); // Monday
			caldroidFragment.setArguments(args);
		}
		
		caldroidFragment.setCaldroidListener(new CaldroidListener()
			{

				public void onSelectDate(Date date, View view)
				{
					// TODO: Implement this method
					filterForDate(date);
				}
				
			
		});

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();
		
	    list = (ListView)findViewById(R.id.list);
		adapter = new AgendaAdapter(this);
		profile = getIntent().getStringExtra("profile");
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
										int position, long arg3)
				{

					if (adapter.getItem(position) instanceof AgendaItem)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(AgendaActivity.this);

						builder.setTitle(((AgendaItem) adapter
										 .getItem(position)).getEpisodeName());
						String plot = ((AgendaItem) adapter
							.getItem(position)).getOverview();
						builder.setMessage(plot.length() > 0 ? plot : "Plot not available");

						builder.setPositiveButton(
							getResources().getString(android.R.string.ok),
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,
													int which)
								{

								}
							});

						AlertDialog alert = builder.create();

						alert.show();
					}
				}

			});
		new LoadEpisodes().execute();
	}

	private List<Agenda> getItems()
	{
		a = new ArrayList<Agenda>();
		List<Show> shows = db.getAllShows("", profile, "id", "");
		List<String> showTitles = new ArrayList<String>();
		for(Show s : shows){
			List<EpisodeItem> episodes = db.getAllEpisodes(s.getSeriesId()+"", profile);
			for(EpisodeItem e : episodes){
				if(e.getSeason() != 0){
					try{
						Date firstAired = Constants.df.parse(e.getFirstAired());
						if(new Date().before(firstAired) || (new Date().getTime() / (1000*60*60*24)) == (firstAired.getTime()/ (1000*60*60*24)))
						{
							if (!showTitles.contains(s.getSeriesName()))
							{
								a.add(new AgendaSection(s.getSeriesName()));
								showTitles.add(s.getSeriesName());
							}
							a.add(new AgendaItem(e.getEpisodeName(), s.getBanner(), EpisodesAdapter.episode(e)[0], e.getFirstAired(), e.getOverview()));
							caldroidFragment.setBackgroundResourceForDate( R.color.caldroid_holo_blue_light, firstAired);
							caldroidFragment.setTextColorForDate(R.color.caldroid_white, firstAired);
						}
					}
					catch(Exception ex){
						
					}
				}
			}
		}
		
		
		return a;
	}

	private void filterForDate(Date date)
	{
		adapter.clear();
		for(Agenda agenda : a)
		{
			if(agenda instanceof AgendaItem)
			{
				try
				{
					Date firstAired = Constants.df.parse(((AgendaItem)agenda).getAirDate());
					if(date.compareTo(firstAired) == 0)
					{
						adapter.add(agenda);
					}
				}
				catch (ParseException e)
				{
					
				}
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public class LoadEpisodes extends AsyncTask<String, Void, List<Agenda>>
	{

		@Override
		protected List<Agenda> doInBackground(String... args)
		{
			return getItems();
		}

		@Override
		protected void onPreExecute()
		{
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onPostExecute(List<Agenda> result)
		{
			adapter.clear();
			for(Agenda a : result){
				adapter.add(a);
			}
			adapter.notifyDataSetChanged();
			setProgressBarIndeterminateVisibility(false);
			caldroidFragment.refreshView();
			//Tools.setListViewHeightBasedOnChildren(list);
			//Tools.setListViewHeightBasedOnChildren(((GridView)findViewById(R.id.calendar_gridview))
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		menu.add(0, 0, 0, "ALL").setShowAsAction(
			MenuItem.SHOW_AS_ACTION_ALWAYS);
		/*
		 * menu.add(0, 1, 1, "Download Header")
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		 */
		return true;
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
			case 0:
			adapter.clear();
			adapter.addAll(a);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

		@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
	}

}
