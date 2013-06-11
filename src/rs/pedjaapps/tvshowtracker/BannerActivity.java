package rs.pedjaapps.tvshowtracker;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class BannerActivity extends SherlockActivity {

	BannersAdapter adapter;
	GridView list;
	DatabaseHandler db;
	String extStorage = Environment.getExternalStorageDirectory().toString();
	String seriesId;
	String profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!Tools.isNetworkAvailable(this)){
			Toast.makeText(this, "No Internet Connection!\nPlease connect to internet and try again!", Toast.LENGTH_LONG).show();
			finish();
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_banner);
		adapter = new BannersAdapter(this, R.layout.banner_row);
		list = (GridView)findViewById(R.id.list);
		list.setAdapter(adapter);
		db = new DatabaseHandler(this);
		seriesId = getIntent().getStringExtra("seriesId");
		String type = getIntent().getStringExtra("type");
		profile = getIntent().getStringExtra("profile");
		new GetBanners().execute(seriesId, type);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				new DownloadBanner().execute(position);
				Tools.setRefresh(true);
				}
		});
	}

	public class GetBanners extends AsyncTask<String, Void, List<Show>>
	{

		@Override
		protected List<Show> doInBackground(String... args)
		{
			List<Show> entry = new ArrayList<Show>();
			
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+args[0]+"/banners.xml"); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			
			NodeList nl = doc.getElementsByTagName("Banner");
			if(args[1].equals("banner")){	   
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
					if(parser.getValue(e, "BannerType").equals("series"))
					{
						entry.add(new Show(parser.getValue(e, "BannerPath")));
					}
				
				
			  }
			}
			else{
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
						if(parser.getValue(e, "BannerType").equals("fanart"))
						{
							entry.add(new Show(parser.getValue(e, "BannerPath")));
						}
					
					
				  }
			}
			return entry;
		}

		@Override
		protected void onPreExecute(){
			setProgressBarIndeterminateVisibility(true);
			 }
		
		@Override
		protected void onPostExecute(List<Show> result)
		{
			adapter.clear();
			for (Show entry : result) {
				adapter.add(entry);
			}
			adapter.notifyDataSetChanged();
			 setProgressBarIndeterminateVisibility(false);
		}
	}	

	public class DownloadBanner extends AsyncTask<Integer, Void, String>
	{

		@Override
		protected String doInBackground(Integer... args)
		{
			
			Show show = db.getShow(seriesId, profile);
			Tools.DownloadFromUrl("http://thetvdb.com/banners/"+adapter.getItem(args[0]).getBanner(), extStorage+"/TVST"+show.getBanner().substring(show.getBanner().lastIndexOf("/"), show.getBanner().length()));
			
			return "";
		}

		@Override
		protected void onPreExecute(){
			setProgressBarIndeterminateVisibility(true);
			 }
		
		@Override
		protected void onPostExecute(String result)
		{
			 setProgressBarIndeterminateVisibility(false);
			 finish();
		}
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
