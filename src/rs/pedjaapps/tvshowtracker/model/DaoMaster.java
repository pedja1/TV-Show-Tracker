package rs.pedjaapps.tvshowtracker.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.SyncLogDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 3): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        UserDao.createTable(db, ifNotExists);
        ShowDao.createTable(db, ifNotExists);
        ImageDao.createTable(db, ifNotExists);
        ActorDao.createTable(db, ifNotExists);
        GenreDao.createTable(db, ifNotExists);
        EpisodeDao.createTable(db, ifNotExists);
        SyncLogDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        UserDao.dropTable(db, ifExists);
        ShowDao.dropTable(db, ifExists);
        ImageDao.dropTable(db, ifExists);
        ActorDao.dropTable(db, ifExists);
        GenreDao.dropTable(db, ifExists);
        EpisodeDao.dropTable(db, ifExists);
        SyncLogDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserDao.class);
        registerDaoClass(ShowDao.class);
        registerDaoClass(ImageDao.class);
        registerDaoClass(ActorDao.class);
        registerDaoClass(GenreDao.class);
        registerDaoClass(EpisodeDao.class);
        registerDaoClass(SyncLogDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
