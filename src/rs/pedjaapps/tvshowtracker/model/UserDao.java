package rs.pedjaapps.tvshowtracker.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import rs.pedjaapps.tvshowtracker.model.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, String> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Username = new Property(0, String.class, "username", true, "USERNAME");
        public final static Property Password = new Property(1, String.class, "password", false, "PASSWORD");
        public final static Property Full_name = new Property(2, String.class, "full_name", false, "FULL_NAME");
        public final static Property Gender = new Property(3, String.class, "gender", false, "GENDER");
        public final static Property Age = new Property(4, Integer.class, "age", false, "AGE");
        public final static Property Location = new Property(5, String.class, "location", false, "LOCATION");
        public final static Property About = new Property(6, String.class, "about", false, "ABOUT");
        public final static Property Joined = new Property(7, Long.class, "joined", false, "JOINED");
        public final static Property Last_login = new Property(8, Long.class, "last_login", false, "LAST_LOGIN");
        public final static Property Avatar = new Property(9, String.class, "avatar", false, "AVATAR");
        public final static Property Url = new Property(10, String.class, "url", false, "URL");
        public final static Property Share_text_watched = new Property(11, String.class, "share_text_watched", false, "SHARE_TEXT_WATCHED");
        public final static Property Share_text_watching = new Property(12, String.class, "share_text_watching", false, "SHARE_TEXT_WATCHING");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'USERNAME' TEXT PRIMARY KEY NOT NULL ," + // 0: username
                "'PASSWORD' TEXT NOT NULL ," + // 1: password
                "'FULL_NAME' TEXT," + // 2: full_name
                "'GENDER' TEXT," + // 3: gender
                "'AGE' INTEGER," + // 4: age
                "'LOCATION' TEXT," + // 5: location
                "'ABOUT' TEXT," + // 6: about
                "'JOINED' INTEGER," + // 7: joined
                "'LAST_LOGIN' INTEGER," + // 8: last_login
                "'AVATAR' TEXT," + // 9: avatar
                "'URL' TEXT," + // 10: url
                "'SHARE_TEXT_WATCHED' TEXT," + // 11: share_text_watched
                "'SHARE_TEXT_WATCHING' TEXT);"); // 12: share_text_watching
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUsername());
        stmt.bindString(2, entity.getPassword());
 
        String full_name = entity.getFull_name();
        if (full_name != null) {
            stmt.bindString(3, full_name);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(4, gender);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(5, age);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(6, location);
        }
 
        String about = entity.getAbout();
        if (about != null) {
            stmt.bindString(7, about);
        }
 
        Long joined = entity.getJoined();
        if (joined != null) {
            stmt.bindLong(8, joined);
        }
 
        Long last_login = entity.getLast_login();
        if (last_login != null) {
            stmt.bindLong(9, last_login);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(10, avatar);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(11, url);
        }
 
        String share_text_watched = entity.getShare_text_watched();
        if (share_text_watched != null) {
            stmt.bindString(12, share_text_watched);
        }
 
        String share_text_watching = entity.getShare_text_watching();
        if (share_text_watching != null) {
            stmt.bindString(13, share_text_watching);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.getString(offset + 0), // username
            cursor.getString(offset + 1), // password
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // full_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // gender
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // age
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // location
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // about
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // joined
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // last_login
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // avatar
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // url
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // share_text_watched
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // share_text_watching
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUsername(cursor.getString(offset + 0));
        entity.setPassword(cursor.getString(offset + 1));
        entity.setFull_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGender(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAge(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setLocation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAbout(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setJoined(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setLast_login(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setAvatar(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUrl(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setShare_text_watched(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setShare_text_watching(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(User entity, long rowId) {
        return entity.getUsername();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(User entity) {
        if(entity != null) {
            return entity.getUsername();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
