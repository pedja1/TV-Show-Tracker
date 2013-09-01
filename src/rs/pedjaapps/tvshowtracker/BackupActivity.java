package rs.pedjaapps.tvshowtracker;

import android.app.*;
import android.os.*;
import android.view.View.*;
import android.widget.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.tvshowtracker.model.*;
import rs.pedjaapps.tvshowtracker.utils.*;

import rs.pedjaapps.tvshowtracker.model.JsonObject;
import android.view.*;

public class BackupActivity extends BaseActivity implements OnClickListener
{

    @Override
	protected void onCreate(Bundle sis)
	{
		super.onCreate(sis);
		setContentView(R.layout.activity_backup);
		Button backup = (Button)findViewById(R.id.backup);
		Button restore = (Button)findViewById(R.id.restore);

		backup.setOnClickListener(this);
		restore.setOnClickListener(this);
	}
	
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.backup:
				new Backuper().execute(true);
				break;
			case R.id.restore:
				new Backuper().execute(false);
				break;

		}
	}

	private JsonObject dbToJson()
	{

		List<String> profiles = db.getAllProfiles();
		List<JsonShow> shows = new ArrayList<JsonShow>();

		for (String p : profiles)
		{
			List<Show> tmpShow = db.getAllShows("", p);
			for (Show s : tmpShow)
			{
				/*List<EpisodeItem> tmpEpisodes = db.getAllEpisodes(s.getSeriesId()+"", p);
				 List<JsonObject.JsonEpisode> episodes = new ArrayList<JsonObject.JsonEpisode>();
				 for(EpisodeItem e : tmpEpisodes)
				 {

				 }*/
				shows.add(new JsonShow(s, db.getAllEpisodes(s.getSeriesId() + "", p), db.getAllActors(s.getSeriesId() + "", p)));
			}
		}

		return new JsonObject(shows, profiles);
	}

	private class Backuper extends AsyncTask<Boolean,String, Boolean>
	{
        boolean isBackup;

		ProgressDialog pd;
		@Override
		protected Boolean doInBackground(Boolean[] params)
		{
			isBackup = params[0];
			Gson gson = new Gson();
			if (params[0])
			{
			    String data = gson.toJson(dbToJson());
			    FileSystem.WriteFile(new File(Environment.getExternalStorageDirectory() + "/tvst/backup.json"), data);
			}
			else
			{
				db.wipeDatabase();
				db = new DatabaseHandler(BackupActivity.this);
				String data = FileSystem.readFile(Environment.getExternalStorageDirectory() + "/tvst/", "backup.json");
				JsonObject obj = gson.fromJson(data, JsonObject.class);
				int pP = 0;
				int pS = obj.getProfiles().size();
				int sP = 0;
				int sS = obj.getShows().size();
				int eP = 0;
				int eS = 0;
				int aP = 0;
				int aS = 0;
				for (String p : obj.getProfiles())
				{
					db.addProfile(p);
					setProgress(pP, pS, sP, sS, eP, eS, aP, aS);
					pP++;
				}
                List<Show> shows = new ArrayList<Show>();
                List<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
                List<Actor> actors = new ArrayList<Actor>();
				for (JsonShow s : obj.getShows())
				{
					
					shows.add(s.getShow());
					setProgress(pP, pS, sP, sS, eP, eS, aP, aS);
					sP++;
					eS = s.getEpisodes().size();
					eP = 0;
					aS = s.getActors().size();
					aP = 0;
					for (EpisodeItem e: s.getEpisodes())
					{
						episodes.add(e);
						setProgress(pP, pS, sP, sS, eP, eS, aP, aS);
						eP++;
					}
					for (Actor a : s.getActors())
					{
						actors.add(a);
						setProgress(pP, pS, sP, sS, eP, eS, aP, aS);
						aP++;
					}
				}
                db.insertActors(actors);
                db.insertEpisodes(episodes);
                db.insertShows(shows);

			}
			return null;
		}
		
		private void setProgress(int pP,
		int pS,
		int sP,
		int sS,
		int eP,
		int eS,
		int aP,
		int aS)
		{
			publishProgress("Backup in progress...\n"
			+"Profile: " + " ("+pP+"/"+pS+")\n"+
			"Show: " + " ("+sP+"/"+sS+")\n"+
			"Episode: " + " ("+eP+"/"+eS+")\n"+
			"Actor: " + " ("+aP+"/"+aS+")");
		}

		@Override
		protected void onProgressUpdate(String... progress)
		{
			System.out.println(progress);
			pd.setMessage(progress[0]);
		}
		
		@Override
		protected void onPreExecute()
		{
			pd = new ProgressDialog(BackupActivity.this);
			pd.setMessage("Please wait...");
			pd.show();
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (pd != null && pd.isShowing())
			{
				pd.dismiss();
			}
			Toast.makeText(BackupActivity.this, isBackup ? "Backup Complete" : "Restore Complete", Toast.LENGTH_LONG).show();
		}

	}
}
