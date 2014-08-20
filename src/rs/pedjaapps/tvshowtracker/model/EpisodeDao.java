package rs.pedjaapps.tvshowtracker.model;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import rs.pedjaapps.tvshowtracker.model.Episode;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table EPISODE.
*/
public class EpisodeDao extends AbstractDao<Episode, Long> {

    public static final String TABLENAME = "EPISODE";

    /**
     * Properties of entity Episode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Season = new Property(1, int.class, "season", false, "SEASON");
        public final static Property Episode = new Property(2, Integer.class, "episode", false, "EPISODE");
        public final static Property Tvdb_id = new Property(3, Integer.class, "tvdb_id", false, "TVDB_ID");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Overview = new Property(5, String.class, "overview", false, "OVERVIEW");
        public final static Property First_aired = new Property(6, Long.class, "first_aired", false, "FIRST_AIRED");
        public final static Property Url = new Property(7, String.class, "url", false, "URL");
        public final static Property Screen = new Property(8, String.class, "screen", false, "SCREEN");
        public final static Property Rating = new Property(9, Integer.class, "rating", false, "RATING");
        public final static Property Votes = new Property(10, Integer.class, "votes", false, "VOTES");
        public final static Property Loved = new Property(11, Integer.class, "loved", false, "LOVED");
        public final static Property Hated = new Property(12, Integer.class, "hated", false, "HATED");
        public final static Property Watched = new Property(13, boolean.class, "watched", false, "WATCHED");
        public final static Property Show_id = new Property(14, long.class, "show_id", false, "SHOW_ID");
    };

    private Query<Episode> show_EpisodesQuery;

    public EpisodeDao(DaoConfig config) {
        super(config);
    }
    
    public EpisodeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EPISODE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SEASON' INTEGER NOT NULL ," + // 1: season
                "'EPISODE' INTEGER," + // 2: episode
                "'TVDB_ID' INTEGER," + // 3: tvdb_id
                "'TITLE' TEXT," + // 4: title
                "'OVERVIEW' TEXT," + // 5: overview
                "'FIRST_AIRED' INTEGER," + // 6: first_aired
                "'URL' TEXT," + // 7: url
                "'SCREEN' TEXT," + // 8: screen
                "'RATING' INTEGER," + // 9: rating
                "'VOTES' INTEGER," + // 10: votes
                "'LOVED' INTEGER," + // 11: loved
                "'HATED' INTEGER," + // 12: hated
                "'WATCHED' INTEGER NOT NULL ," + // 13: watched
                "'SHOW_ID' INTEGER NOT NULL );"); // 14: show_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EPISODE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Episode entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSeason());
 
        Integer episode = entity.getEpisode();
        if (episode != null) {
            stmt.bindLong(3, episode);
        }
 
        Integer tvdb_id = entity.getTvdb_id();
        if (tvdb_id != null) {
            stmt.bindLong(4, tvdb_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
 
        String overview = entity.getOverview();
        if (overview != null) {
            stmt.bindString(6, overview);
        }
 
        Long first_aired = entity.getFirst_aired();
        if (first_aired != null) {
            stmt.bindLong(7, first_aired);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(8, url);
        }
 
        String screen = entity.getScreen();
        if (screen != null) {
            stmt.bindString(9, screen);
        }
 
        Integer rating = entity.getRating();
        if (rating != null) {
            stmt.bindLong(10, rating);
        }
 
        Integer votes = entity.getVotes();
        if (votes != null) {
            stmt.bindLong(11, votes);
        }
 
        Integer loved = entity.getLoved();
        if (loved != null) {
            stmt.bindLong(12, loved);
        }
 
        Integer hated = entity.getHated();
        if (hated != null) {
            stmt.bindLong(13, hated);
        }
        stmt.bindLong(14, entity.getWatched() ? 1l: 0l);
        stmt.bindLong(15, entity.getShow_id());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Episode readEntity(Cursor cursor, int offset) {
        Episode entity = new Episode( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // season
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // episode
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // tvdb_id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // title
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // overview
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // first_aired
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // url
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // screen
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // rating
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // votes
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // loved
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // hated
            cursor.getShort(offset + 13) != 0, // watched
            cursor.getLong(offset + 14) // show_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Episode entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSeason(cursor.getInt(offset + 1));
        entity.setEpisode(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setTvdb_id(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setOverview(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFirst_aired(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setScreen(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRating(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setVotes(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setLoved(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setHated(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setWatched(cursor.getShort(offset + 13) != 0);
        entity.setShow_id(cursor.getLong(offset + 14));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Episode entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Episode entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "episodes" to-many relationship of Show. */
    public List<Episode> _queryShow_Episodes(long show_id) {
        synchronized (this) {
            if (show_EpisodesQuery == null) {
                QueryBuilder<Episode> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Show_id.eq(null));
                show_EpisodesQuery = queryBuilder.build();
            }
        }
        Query<Episode> query = show_EpisodesQuery.forCurrentThread();
        query.setParameter(0, show_id);
        return query.list();
    }

}