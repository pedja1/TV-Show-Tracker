package rs.pedjaapps.tvshowtracker.data;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pedja on 5.11.14. 16.06.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class MemCache
{
    private static MemCache instance;

    private static final long DEF_MAX_CACHE_AGE = 24 * 60 * 60 * 60 * 1000l;//24h

    private ConcurrentHashMap<String, CacheObject> cache;

    public MemCache()
    {
        this.cache = new ConcurrentHashMap<>();
    }

    public static MemCache getInstance()
    {
        if(instance == null)instance = new MemCache();
        return instance;
    }

    /**
     * Add object to cache.<br>
     * @param maxCacheAge max duration that this item will be 'valid' in ms*/
    public <T> void addToCache(String key, T object, long maxCacheAge)
    {
        if(object == null || key == null)return;
        CacheObject<T> co = new CacheObject<>(System.currentTimeMillis(), object, maxCacheAge);
        cache.put(key, co);
    }

    /**
     * Calls {@link #addToCache(String, T, long)} with default cache age value*/
    public <T> void addToCache(String key, T object)
    {
        addToCache(key, object, DEF_MAX_CACHE_AGE);
    }

    /**
     * Remove object from with the given key*/
    public void removeShowFromCache(String key)
    {
        cache.remove(key);
    }

    /**
     * Remove all objects from cache that match pattern
     * This will iterate through cache keyset and check every key*/
    public void removeShowFromCache(Pattern pattern)
    {
        Set<String> keys = cache.keySet();
        for(String key : keys)
        {
            Matcher matcher = pattern.matcher(key);
            if(matcher.matches())
            {
                cache.remove(key);
            }
        }
    }

    public <T> T getFromCache(String key, Class<T> type)
    {
        CacheObject<T> co = cache.get(key);
        if(co == null)return null;
        if(co.isExpired())
        {
            removeShowFromCache(key);
            return null;
        }
        return co.object;
    }

    public <T> T getFromCache(String key)
    {
        CacheObject<T> co = cache.get(key);
        if(co == null)return null;
        if(co.isExpired())
        {
            removeShowFromCache(key);
            return null;
        }
        return co.object;
    }

    /**
     * Clear underlaying cache hashmap*/
    public void flushCache()
    {
        cache.clear();
    }

    private static class CacheObject<T>
    {
        long addedTs;
        T object;
        long maxCacheAge = DEF_MAX_CACHE_AGE;

        public CacheObject(long addedTs, T object)
        {
            this.addedTs = addedTs;
            this.object = object;
        }

        public CacheObject(long addedTs, T object, long maxCacheAge)
        {
            this.addedTs = addedTs;
            this.object = object;
            this.maxCacheAge = maxCacheAge;
        }

        public boolean isExpired()
        {
            return addedTs + maxCacheAge < System.currentTimeMillis();
        }
    }
}
