package rs.pedjaapps.tvshowtracker.utils;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

import java.util.*;

import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.EpisodeItem;
import rs.pedjaapps.tvshowtracker.model.Show;

public class DatabaseHandler extends SQLiteOpenHelper
{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "tvst.db";

	// table names
	private static final String TABLE_SERIES = "series";
	private static final String TABLE_PROFILES = "profiles";
	private static final String TABLE_EPISODES = "episodes";
	private static final String TABLE_ACTORS = "actors";

	// private static final String TABLE_ITEM = "item_table";
	// Table Columns names

	private static final String[] show_filds = { "_id", "series_name",
			"series_id", "language", "banner", "network", "first_aired",
			"imdb_id", "overview", "rating", "runtime", "status", "fanart",
			"ignore_agenda", "hide_from_list", "updated", "actors",
			"profile_name" };
	private static final String[] episode_filds = { "_id", "episode", "season",
			"episode_name", "first_aired", "imdb_id", "overview", "rating",
			"watched", "episode_id", "seriesId", "profile_name" };
	private static final String[] actors_filds = { "_id", "actor_id", "name",
			"role", "image", "seriesId", "profile_name" };
	private static final String[] profile_filds = { "_id", "name" };

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db)
	{

		String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
				+ profile_filds[0] + " INTEGER PRIMARY KEY," + profile_filds[1]
				+ " TEXT" + ")";

		String CREATE_SERIES_TABLE = "CREATE TABLE " + TABLE_SERIES + "("
				+ show_filds[0] + " INTEGER PRIMARY KEY," + show_filds[1]
				+ " TEXT," + show_filds[2] + " INTEGER," + show_filds[3]
				+ " TEXT," + show_filds[4] + " TEXT," + show_filds[5]
				+ " TEXT," + show_filds[6] + " TEXT," + show_filds[7]
				+ " TEXT," + show_filds[8] + " TEXT," + show_filds[9]
				+ " DOUBLE," + show_filds[10] + " INTEGER," + show_filds[11]
				+ " TEXT," + show_filds[12] + " TEXT," + show_filds[13]
				+ " BOOLEAN," + show_filds[14] + " BOOLEAN," + show_filds[15]
				+ " TEXT," + show_filds[16] + " TEXT," + show_filds[17]
				+ " TEXT" + ")";
		String CREATE_EPISODE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EPISODES
		    + "(" + episode_filds[0] + " INTEGER PRIMARY KEY,"
			+ episode_filds[1] + " INTEGER," + episode_filds[2]
			+ " INTEGER," + episode_filds[3] + " TEXT," + episode_filds[4]
			+ " TEXT," + episode_filds[5] + " TEXT," + episode_filds[6]
			+ " TEXT," + episode_filds[7] + " DOUBLE," + episode_filds[8]
			+ " BOOLEAN," + episode_filds[9] + " INTEGER,"
			+ episode_filds[10] + " TEXT," + episode_filds[11] + " TEXT"+ ")";

		String CREATE_ACTORS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTORS
			+ "(" + actors_filds[0] + " INTEGER PRIMARY KEY,"
			+ actors_filds[1] + " TEXT," + actors_filds[2] + " TEXT,"
			+ actors_filds[3] + " TEXT," + actors_filds[4] + " TEXT,"
			+ actors_filds[5] + " TEXT," + actors_filds[6] + " TEXT"+ ")";
	
		db.execSQL(CREATE_PROFILES_TABLE);
		ContentValues values = new ContentValues();
		values.put(profile_filds[1], "Default");

		// Inserting Row
		db.insert(TABLE_PROFILES, null, values);
		db.execSQL(CREATE_SERIES_TABLE);
		db.execSQL(CREATE_EPISODE_TABLE);
		db.execSQL(CREATE_ACTORS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	 public void wipeDatabase()
	 {
		 SQLiteDatabase db = this.getWritableDatabase();
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);
		 onCreate(db);
		 db.close();
	 }
	 
	public synchronized void addProfile(String profileName)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(profile_filds[1], profileName);

		// Inserting Row
		db.insert(TABLE_PROFILES, null, values);
		db.close(); // Closing database connection
	}

	public synchronized void addShow(Show s)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(show_filds[1], s.getSeriesName());
		values.put(show_filds[2], s.getSeriesId());
		values.put(show_filds[3], s.getLanguage());
		values.put(show_filds[4], s.getBanner());
		values.put(show_filds[5], s.getNetwork());
		values.put(show_filds[6], s.getFirstAired());
		values.put(show_filds[7], s.getImdbId());
		values.put(show_filds[8], s.getOverview());
		values.put(show_filds[9], s.getRating());
		values.put(show_filds[10], s.getRuntime());
		values.put(show_filds[11], s.getStatus());
		values.put(show_filds[12], s.getFanart());
		values.put(show_filds[13], s.isIgnore());
		values.put(show_filds[14], s.isHide());
		values.put(show_filds[15], s.getUpdated());
		values.put(show_filds[16], s.getActors());
		values.put(show_filds[17], s.getProfileName());

		// Inserting Row
		db.insert(TABLE_SERIES, null, values);
		db.close(); // Closing database connection
	}

	public synchronized int updateShow(Show s)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(show_filds[1], s.getSeriesName());
		values.put(show_filds[2], s.getSeriesId());
		values.put(show_filds[3], s.getLanguage());
		values.put(show_filds[4], s.getBanner());
		values.put(show_filds[5], s.getNetwork());
		values.put(show_filds[6], s.getFirstAired());
		values.put(show_filds[7], s.getImdbId());
		values.put(show_filds[8], s.getOverview());
		values.put(show_filds[9], s.getRating());
		values.put(show_filds[10], s.getRuntime());
		values.put(show_filds[11], s.getStatus());
		values.put(show_filds[12], s.getFanart());
		values.put(show_filds[13], s.isIgnore());
		values.put(show_filds[14], s.isHide());
		values.put(show_filds[15], s.getUpdated());
		values.put(show_filds[16], s.getActors());
		values.put(show_filds[17], s.getProfileName());

		int count = db.update(TABLE_SERIES, values, show_filds[2] + " = ?",
				new String[] { s.getSeriesId()+"" });
		db.close();
		return count;
	}

	/**
	 * @param filter
	 *            Can be either all, continuing, or ended
	 */
	public synchronized List<Show> getAllShows(String filter, String profile)
	{
		long startTime = System.currentTimeMillis();
		List<Show> shows = new ArrayList<Show>();
		// Select All Query
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT  * FROM " + TABLE_SERIES + " WHERE");

		if (filter.equals("ended"))
		{
			builder.append(" status LIKE \"%Ended%\"");
		}
		else if (filter.equals("continuing"))
		{
			builder.append(" status LIKE \"%Continuing%\"");
		}
		else
		{
			builder.append(" status LIKE \"%\"");
		}
		builder.append(" and profile_name LIKE \"%" + profile + "%\"");
		String selectQuery = builder.toString();// "SELECT  * FROM " +
												// TABLE_SERIES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Show show = new Show();
				show.setId(Integer.parseInt(cursor.getString(0)));
				show.setSeriesName(cursor.getString(1));
				show.setSeriesId(cursor.getInt(2));
				show.setLanguage(cursor.getString(3));
				show.setBanner(cursor.getString(4));
				show.setNetwork(cursor.getString(5));
				show.setFirstAired(cursor.getString(6));
				show.setImdbId(cursor.getString(7));
				show.setOverview(cursor.getString(8));
				show.setRating(cursor.getDouble(9));
				show.setRuntime(cursor.getInt(10));
				show.setStatus(cursor.getString(11));
				show.setFanart(cursor.getString(12));
				show.setIgnore(intToBool(cursor.getInt(13)));
				show.setHide(intToBool(cursor.getInt(14)));
				show.setUpdated(cursor.getString(15));
				show.setActors(cursor.getString(16));
				show.setProfileName(cursor.getString(17));

				shows.add(show);
			}
			while (cursor.moveToNext());
		}

		// return list
		db.close();
		cursor.close();
		Log.d(Constants.LOG_TAG,
				"DatabaseHandler.java > getAllShows(): "
						+ (System.currentTimeMillis() - startTime) + "ms");
		return shows;
	}

	public synchronized boolean showExists(String seriesName, String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SERIES, show_filds, show_filds[1]
				+ "=? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesName }, null, null, null, null);
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}

	public synchronized void deleteSeries(String seriesId, String profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERIES, show_filds[2]
				+ " = ? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesId });

		deleteEpisodes(seriesId, profile);
		deleteActors(seriesId, profile);
		db.close();
	}

	public synchronized void deleteEpisodes(String seriesId, String profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EPISODES, "profile_name = ? and seriesId = ?",
				new String[] { profile, seriesId });
		db.close();
	}

	public synchronized void deleteActors(String seriesId, String profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACTORS, "profile_name = ? and seriesId = ?",
				new String[] { profile, seriesId });
		db.close();
	}

	public synchronized void deleteProfile(String profile)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROFILES, profile_filds[1] + " = ?",
				new String[] { profile });

		db.close();
	}

	public synchronized Show getShow(String seriesId, String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query("series", show_filds, show_filds[2]
				+ "=? and profile_name LIKE \"%" + profile + "%\"",
				new String[] { seriesId }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Show e = new Show(cursor.getInt(0), cursor.getString(1),
				cursor.getString(6), cursor.getString(7), cursor.getString(8),
				cursor.getDouble(9), cursor.getInt(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(12), cursor.getString(5),
				cursor.getInt(10), cursor.getString(11),
				intToBool(cursor.getInt(13)), intToBool(cursor.getInt(14)),
				cursor.getString(15), cursor.getString(16), cursor.getString(17));
		// return list
		db.close();
		cursor.close();
		return e;
	}

	public synchronized void addEpisode(EpisodeItem e)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(episode_filds[1], e.getEpisode());
		values.put(episode_filds[2], e.getSeason());
		values.put(episode_filds[3], e.getEpisodeName());
		values.put(episode_filds[4], e.getFirstAired());
		values.put(episode_filds[5], e.getImdbId());
		values.put(episode_filds[6], e.getOverview());
		values.put(episode_filds[7], e.getRating());
		values.put(episode_filds[8], e.isWatched());
		values.put(episode_filds[9], e.getEpisodeId());
		values.put(episode_filds[10], e.getSeriesId());
		values.put(episode_filds[11], e.getProfile());

		// Inserting Row
		db.insert(TABLE_EPISODES, null, values);
		db.close(); // Closing database connection
	}

	public synchronized void addActor(Actor a)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(actors_filds[1], a.getActorId());
		values.put(actors_filds[2], a.getName());
		values.put(actors_filds[3], a.getRole());
		values.put(actors_filds[4], a.getImage());
		values.put(actors_filds[5], a.getSeriesId());
		values.put(actors_filds[6], a.getProfile());

		// Inserting Row
		db.insert(TABLE_ACTORS, null, values);
		db.close(); // Closing database connection
	}

	public synchronized EpisodeItem getEpisode(String seriesId, String episodeId,
			String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_EPISODES, episode_filds,
				episode_filds[9] + "=? and profile_name LIKE \"%" + profile
						+ "%\" and seriesId = " + seriesId , new String[] { episodeId }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		EpisodeItem e = new EpisodeItem(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getInt(2), cursor.getInt(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6),
				cursor.getDouble(7), intToBool(cursor.getInt(8)),
				cursor.getInt(9), cursor.getString(11), cursor.getString(10));
		// return list
		db.close();
		cursor.close();
		return e;
	}

	public synchronized Actor getActor(String seriesId, String actorId, String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ACTORS, actors_filds,
				actors_filds[1] + "=? and profile_name LIKE \"%" + profile
						+ "%\" and seriesId = " + seriesId, new String[] { actorId }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		Actor a = new Actor(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(6), cursor.getString(5));
		// return list
		db.close();
		cursor.close();
		return a;
	}

	public synchronized List<String> getAllProfiles()
	{
		List<String> profiles = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM profiles";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst())
		{
			do
			{
				profiles.add(cursor.getString(1));
			}
			while (cursor.moveToNext());
		}

		// return list
		db.close();
		cursor.close();
		return profiles;
	}

	public synchronized List<EpisodeItem> getAllEpisodes(String seriesId, String profile)
	{
		List<EpisodeItem> episodeItems = new ArrayList<EpisodeItem>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EPISODES
				+ " WHERE profile_name LIKE \"%" + profile + "%\" and seriesId = "+seriesId;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst())
		{
			do
			{
				EpisodeItem episodeItem = new EpisodeItem();
				episodeItem.setId(Integer.parseInt(cursor.getString(0)));
				episodeItem.setEpisode(cursor.getInt(1));
				episodeItem.setSeason(cursor.getInt(2));
				episodeItem.setEpisodeName(cursor.getString(3));
				episodeItem.setFirstAired(cursor.getString(4));
				episodeItem.setImdbId(cursor.getString(5));
				episodeItem.setOverview(cursor.getString(6));
				episodeItem.setRating(cursor.getDouble(7));
				episodeItem.setWatched(intToBool(cursor.getInt(8)));
				episodeItem.setEpisodeId(cursor.getInt(9));
				episodeItem.setSeriesId(cursor.getString(10));
				episodeItem.setProfile(cursor.getString(11));

				// Adding to list
				episodeItems.add(episodeItem);
			}
			while (cursor.moveToNext());
		}

		// return list
		db.close();
		cursor.close();
		return episodeItems;
	}

	public synchronized List<Actor> getAllActors(String seriesId, String profile)
	{
		List<Actor> actors = new ArrayList<Actor>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ACTORS
				+ " WHERE profile_name LIKE \"%" + profile + "%\" and seriesId = " + seriesId;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst())
		{
			do
			{
				Actor actor = new Actor();
				actor.setId(Integer.parseInt(cursor.getString(0)));
				actor.setActorId(cursor.getString(1));
				actor.setName(cursor.getString(2));
				actor.setRole(cursor.getString(3));
				actor.setImage(cursor.getString(4));
				actor.setSeriesId(cursor.getString(5));
				actor.setProfile(cursor.getString(6));

				// Adding to list
				actors.add(actor);
			}
			while (cursor.moveToNext());
		}

		// return list
		db.close();
		cursor.close();
		return actors;
	}

	/*
	 * public void deleteEpisode(EpisodeItem e, String seriesId) {
	 * SQLiteDatabase db = this.getWritableDatabase();
	 * db.delete("episodes_"+seriesId, episode_filds[9] + " = ?", new String[] {
	 * String.valueOf(e.getEpisodeId()) });
	 * 
	 * db.close(); }
	 */

	public synchronized boolean episodeExists(String seriesId, String episodeId,
			String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_EPISODES, episode_filds,
				episode_filds[9] + "=? and profile_name LIKE \"%" + profile
						+ "%\" and seriesId = "+ seriesId, new String[] { episodeId }, null, null, null,
				null);
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}

	public synchronized boolean actorExists(String seriesId, String actorId, String profile)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ACTORS, actors_filds,
				actors_filds[1] + "=? and profile_name LIKE \"%" + profile
						+ "%\" and seriesId = " + seriesId, new String[] { actorId }, null, null, null,
				null);
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}

	public synchronized int updateEpisode(EpisodeItem e)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(episode_filds[1], e.getEpisode());
		values.put(episode_filds[2], e.getSeason());
		values.put(episode_filds[3], e.getEpisodeName());
		values.put(episode_filds[4], e.getFirstAired());
		values.put(episode_filds[5], e.getImdbId());
		values.put(episode_filds[6], e.getOverview());
		values.put(episode_filds[7], e.getRating());
		values.put(episode_filds[8], e.isWatched());
		values.put(episode_filds[9], e.getEpisodeId());
		values.put(episode_filds[10], e.getSeriesId());
		values.put(episode_filds[11], e.getProfile());

		int count = db.update(TABLE_EPISODES, values, episode_filds[9]
				+ " = ?  and profile_name LIKE \"%" + e.getProfile() + "%\" and seriesId = " + e.getSeriesId(),
				new String[] { e.getEpisodeId()+"" });
		db.close();
		return count;
	}

	public synchronized int updateActor(Actor a)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(actors_filds[1], a.getActorId());
		values.put(actors_filds[2], a.getName());
		values.put(actors_filds[3], a.getRole());
		values.put(actors_filds[4], a.getImage());
		values.put(actors_filds[5], a.getSeriesId());
		values.put(actors_filds[6], a.getProfile());

		int count = db.update(TABLE_ACTORS, values, actors_filds[1]
				+ " = ?  and profile_name LIKE \"%" + a.getProfile() + "%\" and seriesId = "+a.getSeriesId(),
				new String[] { a.getActorId() });
		db.close();
		return count;
	}

	public synchronized int getEpisodesCount(String seriesId, String profile)
	{
		String countQuery = "SELECT  * FROM "+ TABLE_EPISODES
				+ " WHERE profile_name LIKE \"%" + profile + "%\" and seriesId = " + seriesId;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	private boolean intToBool(int i)
	{
		if (i == 1)
		{
			return true;
		}
		return false;
	}

}
