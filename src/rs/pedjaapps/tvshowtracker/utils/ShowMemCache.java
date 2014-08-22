package rs.pedjaapps.tvshowtracker.utils;

import java.util.concurrent.ConcurrentHashMap;

import rs.pedjaapps.tvshowtracker.model.Show;

/**
 * Created by pedja on 22.8.14. 13.18.
 * This class is part of the .tvst
 * Copyright © 2014 ${OWNER}
 */
public class ShowMemCache
{
    private static ShowMemCache instance;

    private ConcurrentHashMap<Integer, Show> cache;

    public ShowMemCache()
    {
        this.cache = new ConcurrentHashMap<Integer, Show>();
    }

    public static ShowMemCache getInstance()
    {
        if(instance == null)instance = new ShowMemCache();
        return instance;
    }

    public void addShowToCache(Show show)
    {
        cache.put(show.getTvdb_id(), show);
    }

    public void removeShowFromCache(int tvdbId)
    {
        cache.remove(tvdbId);
    }

    public void removeShowFromCache(Show show)
    {
        cache.remove(show.getTvdb_id());
    }

    public Show getCachedShow(int tvdbId)
    {
        return cache.get(tvdbId);
    }

    public void pop()
    {
        cache.clear();
    }
}
