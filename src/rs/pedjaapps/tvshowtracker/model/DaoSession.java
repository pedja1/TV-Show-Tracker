package rs.pedjaapps.tvshowtracker.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import rs.pedjaapps.tvshowtracker.model.User;
import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.Image;
import rs.pedjaapps.tvshowtracker.model.Actor;
import rs.pedjaapps.tvshowtracker.model.Genre;
import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.SyncLog;

import rs.pedjaapps.tvshowtracker.model.UserDao;
import rs.pedjaapps.tvshowtracker.model.ShowDao;
import rs.pedjaapps.tvshowtracker.model.ImageDao;
import rs.pedjaapps.tvshowtracker.model.ActorDao;
import rs.pedjaapps.tvshowtracker.model.GenreDao;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.SyncLogDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig showDaoConfig;
    private final DaoConfig imageDaoConfig;
    private final DaoConfig actorDaoConfig;
    private final DaoConfig genreDaoConfig;
    private final DaoConfig episodeDaoConfig;
    private final DaoConfig syncLogDaoConfig;

    private final UserDao userDao;
    private final ShowDao showDao;
    private final ImageDao imageDao;
    private final ActorDao actorDao;
    private final GenreDao genreDao;
    private final EpisodeDao episodeDao;
    private final SyncLogDao syncLogDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        showDaoConfig = daoConfigMap.get(ShowDao.class).clone();
        showDaoConfig.initIdentityScope(type);

        imageDaoConfig = daoConfigMap.get(ImageDao.class).clone();
        imageDaoConfig.initIdentityScope(type);

        actorDaoConfig = daoConfigMap.get(ActorDao.class).clone();
        actorDaoConfig.initIdentityScope(type);

        genreDaoConfig = daoConfigMap.get(GenreDao.class).clone();
        genreDaoConfig.initIdentityScope(type);

        episodeDaoConfig = daoConfigMap.get(EpisodeDao.class).clone();
        episodeDaoConfig.initIdentityScope(type);

        syncLogDaoConfig = daoConfigMap.get(SyncLogDao.class).clone();
        syncLogDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        showDao = new ShowDao(showDaoConfig, this);
        imageDao = new ImageDao(imageDaoConfig, this);
        actorDao = new ActorDao(actorDaoConfig, this);
        genreDao = new GenreDao(genreDaoConfig, this);
        episodeDao = new EpisodeDao(episodeDaoConfig, this);
        syncLogDao = new SyncLogDao(syncLogDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Show.class, showDao);
        registerDao(Image.class, imageDao);
        registerDao(Actor.class, actorDao);
        registerDao(Genre.class, genreDao);
        registerDao(Episode.class, episodeDao);
        registerDao(SyncLog.class, syncLogDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        showDaoConfig.getIdentityScope().clear();
        imageDaoConfig.getIdentityScope().clear();
        actorDaoConfig.getIdentityScope().clear();
        genreDaoConfig.getIdentityScope().clear();
        episodeDaoConfig.getIdentityScope().clear();
        syncLogDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ShowDao getShowDao() {
        return showDao;
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

    public ActorDao getActorDao() {
        return actorDao;
    }

    public GenreDao getGenreDao() {
        return genreDao;
    }

    public EpisodeDao getEpisodeDao() {
        return episodeDao;
    }

    public SyncLogDao getSyncLogDao() {
        return syncLogDao;
    }

}
