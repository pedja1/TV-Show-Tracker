package rs.pedjaapps.tvshowtracker;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;


public class DatabaseHandler extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "tvst.db";

    // table names
	private static final String TABLE_SERIES= "series";
	
    //private static final String TABLE_ITEM = "item_table";
    // Table Columns names
	
	
	private static final String[] show_filds = {"_id", "series_name", 
		"series_id", "language", "banner", "network","first_aired", 
		"imdb_id", "overview", "rating","runtime", "status", "fanart", 
		"ignore_agenda", "hide_from_list", "updated", "actors"};
	
	private static final String[] episode_filds = {"_id", "episode", 
		"season", "episode_name", "first_aired", "imdb_id", "overview", 
		"rating", "watched", "episode_id"};
    
	public DatabaseHandler(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
	{
       
		
			
		String CREATE_SERIES_TABLE = "CREATE TABLE " + TABLE_SERIES + "("
			+ show_filds[0] + " INTEGER PRIMARY KEY,"
			+ show_filds[1] + " TEXT,"
			+ show_filds[2] + " INTEGER,"
			+ show_filds[3] + " TEXT,"
			+ show_filds[4] + " TEXT,"
			+ show_filds[5] + " TEXT,"
			+ show_filds[6] + " TEXT,"
		    + show_filds[7] + " TEXT,"
		    + show_filds[8] + " TEXT,"
			+ show_filds[9] + " DOUBLE,"
			+ show_filds[10] + " INTEGER,"
			+ show_filds[11] + " TEXT,"
			+ show_filds[12] + " TEXT,"
			+ show_filds[13] + " BOOLEAN,"
		    + show_filds[14] + " BOOLEAN,"
		    + show_filds[15] + " TEXT,"
		    + show_filds[16] + " TEXT"
			+
			")";

		db.execSQL(CREATE_SERIES_TABLE);
	}

	
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
	
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void createEpisodeTable(String seriesId){
    	SQLiteDatabase db = this.getWritableDatabase();
    	String CREATE_EPISODE_TABLE = "CREATE TABLE IF NOT EXISTS episodes_" + seriesId + "("
    			+ episode_filds[0] + " INTEGER PRIMARY KEY,"
    			+ episode_filds[1] + " INTEGER,"
    			+ episode_filds[2] + " INTEGER," 
    			+ episode_filds[3] + " TEXT,"
    			+ episode_filds[4] + " TEXT,"
    			+ episode_filds[5] + " TEXT," 
    			+ episode_filds[6] + " TEXT,"
    			+ episode_filds[7] + " DOUBLE,"
    			+ episode_filds[8] + " BOOLEAN," 
    			+ episode_filds[9] + " INTEGER"
    			+
    			")";

    		db.execSQL(CREATE_EPISODE_TABLE);
            db.close();
    }
	
	public void addShow(Show s)
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

        // Inserting Row
        db.insert(TABLE_SERIES, null, values);
        db.close(); // Closing database connection
        createEpisodeTable(s.getSeriesId()+"");
    }
	
	public List<Show> getAllShows()
	{
        List<Show> shows = new ArrayList<Show>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
		{
            do {
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
				
                shows.add(show);
            } while (cursor.moveToNext());
        }

        // return list
        db.close();
        cursor.close();
        return shows;
    }
	
	public boolean showExists(String seriesName) {
    	SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERIES, show_filds, show_filds[1] + "=?",
								 new String[] { seriesName }, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
	}
	
	public void deleteSeries(String seriesId)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERIES, show_filds[2] + " = ?",
				  new String[] { seriesId });

		db.execSQL("DROP TABLE IF EXISTS episodes_"+ seriesId);
        db.close();
    }
	public Show getShow(String seriesId)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("series", show_filds, show_filds[2] + "=?",
								 new String[] { seriesId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Show e = new Show(cursor.getInt(0),
        		cursor.getString(1),
        		cursor.getString(6), 
        		cursor.getString(7),
        		cursor.getString(8),
        		cursor.getDouble(9),
        		cursor.getInt(2),
        		cursor.getString(3),
        		cursor.getString(4),
        		cursor.getString(12),
        		cursor.getString(5),
        		cursor.getInt(10),
        		cursor.getString(11),
        		intToBool(cursor.getInt(13)),
        		intToBool(cursor.getInt(14)),
        		cursor.getString(15),
        		cursor.getString(16));
        // return list
        db.close();
        cursor.close();
        return e;
    }
    public void addEpisode(EpisodeItem e, String seriesId)
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

        // Inserting Row
        db.insert("episodes_"+seriesId, null, values);
        db.close(); // Closing database connection
    }
    
    
	public EpisodeItem getEpisode(String seriesId, String episodeId)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("episodes_"+seriesId, episode_filds, episode_filds[9] + "=?",
								 new String[] { episodeId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        EpisodeItem e = new EpisodeItem(Integer.parseInt(cursor.getString(0)),
															cursor.getString(1),
															cursor.getInt(2),
															cursor.getInt(3),
															cursor.getString(4),
															cursor.getString(5),
															cursor.getString(6),
															cursor.getDouble(7),
															intToBool(cursor.getInt(8)),
															cursor.getInt(9)
															);
        // return list
        db.close();
        cursor.close();
        return e;
    }
	
	
	
	public List<EpisodeItem> getAllEpisodes(String seriesId)
	{
        List<EpisodeItem> episodeItems = new ArrayList<EpisodeItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM episodes_" + seriesId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst())
		{
            do {
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
				

                // Adding  to list
                episodeItems.add(episodeItem);
            } while (cursor.moveToNext());
        }

        // return list
        db.close();
        cursor.close();
        return episodeItems;
    }
   
	public void deleteEpisode(EpisodeItem e, String seriesId)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("episodes_"+seriesId, episode_filds[9] + " = ?",
				  new String[] { String.valueOf(e.getEpisodeId()) });
        
        db.close();
    }
	
	
	
	public boolean episodeExists(String seriesId, String episodeId) {
    	SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("episodes_"+seriesId, episode_filds, episode_filds[9] + "=?",
								 new String[] { episodeId }, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
	}
	
	public int updateEpisode(EpisodeItem e, String episodeId, String seriesId)
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
        
        int count = db.update("episodes_"+seriesId, values, episode_filds[9] + " = ?",
						 new String[] { episodeId });
        db.close();
        return count;
        }
		
	
	public int getEpisodesCount(String seriesId)
	{
        String countQuery = "SELECT  * FROM episodes_" + seriesId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
       int count = cursor.getCount();
	  // cursor.close();
      // db.close();
        // return count
        return count;
    }

	private boolean intToBool(int i){
		if(i==1){
			return true;
		}
		return false;
	}
	
}

