package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.content.*;
import android.net.ConnectivityManager;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.http.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.actionbarsherlock.app.SherlockActivity;


public class SearchResults extends SherlockActivity {

	
	ListView searchListView;
	SearchAdapter searchAdapter;
	DatabaseHandler db;
	String extStorage = Environment.getExternalStorageDirectory().toString();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.search_result);
		db = new DatabaseHandler(this);
		
		
		searchListView = (ListView) findViewById(R.id.list);
		
		
		searchAdapter = new SearchAdapter(this, R.layout.search_row);

		searchListView.setAdapter(searchAdapter);

		handleIntent(getIntent());
		
		searchListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(db.showExists(searchAdapter.getItem(position).getSeriesName())==false)
					new DownloadShowInfo().execute(new String[]{searchAdapter.getItem(position).getSeriesId()+"", searchAdapter.getItem(position).getLanguage()});
				else
					Toast.makeText(SearchResults.this, "Show Exists!\nSelect Another", Toast.LENGTH_LONG).show();
			}
			
		});
		


		
}
	public static boolean isNetworkAvailable(Context context) 
	{
	    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
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
			if(isNetworkAvailable(this))
				new TitleSearchParser().execute(new String[] {query.replaceAll(" ", "%20")});
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
			publishProgress(new Integer[] {0,0});
			/*String dataDir = getFilesDir()+"/";
			DownloadFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+args[0]+"/all/"+args[1]+".zip", dataDir+"en.zip");
			Decompress d = new Decompress(dataDir+"en.zip", dataDir); 
			d.unzip(); 
			System.out.println(dataDir);*/
			if(!new File(extStorage+"/TVST").exists()){
				new File(extStorage+"/TVST").mkdir();
			}
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl("http://thetvdb.com/api/"+Constants.apiKey+"/series/"+args[0]+"/all/"+args[1]+".xml"/*dataDir+"en.xml"*/); 
			
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			NodeList nl = doc.getElementsByTagName("Series");
			Element e = (Element) nl.item(0);
			String date = Constants.df.format(new Date());
			String seriesId = parser.getValue(e, "id");
			db.addShow(new Show(parser.getValue(e, "SeriesName"), parser.getValue(e, "FirstAired"), 
					parser.getValue(e, "IMDB_ID"), parser.getValue(e, "Overview"),
					parseRating(parser.getValue(e, "Rating")), Integer.parseInt(parser.getValue(e, "id")),
					parser.getValue(e, "Language"), 
					DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "banner"), extStorage+"/TVST"+parser.getValue(e, "banner").substring(parser.getValue(e, "banner").lastIndexOf("/"))), 
					DownloadFromUrl("http://thetvdb.com/banners/"+parser.getValue(e, "fanart"), extStorage+"/TVST"+parser.getValue(e, "fanart").substring(parser.getValue(e, "fanart").lastIndexOf("/"))), 
					parser.getValue(e, "Network"), parseInt(parser.getValue(e, "Runtime")), 
					parser.getValue(e, "Status"), false, false, date, parser.getValue(e, "Actors")));
			
			nl = doc.getElementsByTagName("Episode");
			
			for(int i =0; i < nl.getLength(); i++){
				e = (Element) nl.item(i);
				if(!parser.getValue(e, "SeasonNumber").equals("0")){
				db.addEpisode(new EpisodeItem(parser.getValue(e, "EpisodeName"),
						parseInt(parser.getValue(e, "EpisodeNumber")), 
						parseInt(parser.getValue(e, "SeasonNumber")),
						parser.getValue(e, "FirstAired"), parser.getValue(e, "IMDB_ID"), 
						parser.getValue(e, "Overview"), parseRating(parser.getValue(e, "Rating")),
						false, parseInt(parser.getValue(e, "id"))), seriesId);
				}
				publishProgress(new Integer[]{1,i,nl.getLength()});
			}
			
			return "";
			
		}
		
		@Override
		protected void onProgressUpdate(Integer[]... progress){
			if((progress[0])[0]==0)
				pd.setMessage("Downloading Show Information");
			else
			{
				pd.setMessage("Downloading Episodes "+(progress[0])[1]+"/"+(progress[0])[2]);
			}
		}
		
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(SearchResults.this);
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
			
		}
	}	
	private double parseRating(String rating){
		try{
			return Double.parseDouble(rating);
		}
		catch(Exception e)
		{
			return 0.0;
		}
	}
	private int parseInt(String runtime){
		try{
			return Integer.parseInt(runtime);
		}
		catch(Exception e){
			return 0;
		}
	}
	public class TitleSearchParser extends AsyncTask<String, Void, List<Show>>
	{

		@Override
		protected List<Show> doInBackground(String... args)
		{
			List<Show> entry = new ArrayList<Show>();
			
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl("http://thetvdb.com/api/GetSeries.php?seriesname="+args[0]); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			NodeList nl = doc.getElementsByTagName("Series");
			     
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				entry.add(new Show(parser.getValue(e, "SeriesName"), parser.getValue(e, "Overview"),
						Integer.parseInt(parser.getValue(e, "seriesid")),
						parser.getValue(e, "language"), parser.getValue(e, "Network"), parser.getValue(e, "FirstAired")));
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
			searchAdapter.clear();
			for (Show entry : result) {
				searchAdapter.add(entry);
			}
			searchAdapter.notifyDataSetChanged();
			 setProgressBarIndeterminateVisibility(false);
		}
	}	

	public static String DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
        try {
        	File mdbDir = new File(Environment.getExternalStorageDirectory() + "/MDb/posters");
		      if(mdbDir.exists()==false){
		      mdbDir.mkdirs();
		      }
                URL url = new URL(imageURL);
                File file = new File(fileName);

                
               /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                }

                /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
			return fileName;

        } catch (IOException e) {
                Log.e("error saving image", e.getMessage());
        	return "";
		}

}
 
}
