package rs.pedjaapps.tvshowtracker.utils;

import java.util.concurrent.ConcurrentHashMap;

import rs.pedjaapps.tvshowtracker.model.Show;
import rs.pedjaapps.tvshowtracker.model.ShowNoDao;

/**
 * Created by pedja on 22.8.14. 13.18.
 * This class is part of the .tvst
 * Copyright © 2014 ${OWNER}
 */
public class ShowMemCache
{
    private static ShowMemCache instance;

    private ConcurrentHashMap<String, ShowNoDao> cache;

    public ShowMemCache()
    {
        this.cache = new ConcurrentHashMap<String, ShowNoDao>();
    }

    public static ShowMemCache getInstance()
    {
        if(instance == null)instance = new ShowMemCache();
        return instance;
    }

    public void addShowToCache(String key, ShowNoDao show)
    {
        if(show == null || key == null)return;
        cache.put(key, show);
    }

    public void removeShowFromCache(String key)
    {
        cache.remove(key);
    }

    public Show getCachedShow(String key)
    {
        return cache.get(key);
    }

    public void pop()
    {
        cache.clear();
    }
}
