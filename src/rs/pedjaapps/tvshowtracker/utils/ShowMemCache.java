package rs.pedjaapps.tvshowtracker.utils;

import java.util.List;
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
	
	private static final long DEF_MAX_SHOW_CACHE_AGE = 24 * 60 * 60 * 60 * 1000l;//24h
	private static final long DEF_MAX_LIST_CACHE_AGE = 24 * 60 * 60 * 60 * 1000l;//24h
	
	private long maxShowCacheAge = DEF_MAX_SHOW_CACHE_AGE;
	private long maxListCacheAge = DEF_MAX_LIST_CACHE_AGE;

	public enum ListKey
	{
		trending
	}
	
    private ConcurrentHashMap<String, CacheObject> ssCache;
	private ConcurrentHashMap<ListKey, CacheObject> listCache;

    public ShowMemCache()
    {
        this.ssCache = new ConcurrentHashMap<>();
		this.listCache = new ConcurrentHashMap<>();
    }

    public static ShowMemCache getInstance()
    {
        if(instance == null)instance = new ShowMemCache();
        return instance;
    }

    public void addShowToCache(String key, Show show)
    {
        if(show == null || key == null)return;
		CacheObject co = new CacheObject(System.currentTimeMillis(), show);
        ssCache.put(key, co);
    }

    public void removeShowFromCache(String key)
    {
        ssCache.remove(key);
    }

    public Show getCachedShow(String key)
    {
        CacheObject co = ssCache.get(key);
		if(co == null)return null;
		if(co.addedTs + maxShowCacheAge < System.currentTimeMillis())
		{
			removeShowFromCache(key);
			return null;
		}
		return co.show;
    }

    public void popShowCache()
    {
        ssCache.clear();
    }
	
	public List<Show> getCachedList(ListKey listKey)
	{
		CacheObject co = listCache.get(listKey);
		if(co == null)return null;
		if(co.addedTs + maxListCacheAge < System.currentTimeMillis())
		{
			removeListFromCache(listKey);
			return null;
		}
		return co.list;
	}
	
	public void addListToCache(ListKey key, List<Show> list)
    {
        if(list == null || key == null || list.isEmpty())return;
		CacheObject co = new CacheObject(System.currentTimeMillis(), list);
        listCache.put(key, co);
    }

    public void removeListFromCache(ListKey key)
    {
        listCache.remove(key);
    }
	
	public void popListCache()
    {
        listCache.clear();
    }
	
	private class CacheObject
	{
		long addedTs;
		Show show;
		List<Show> list;

		public CacheObject(long addedTs, Show show)
		{
			this.addedTs = addedTs;
			this.show = show;
		}
		
		public CacheObject(long addedTs, List<Show> list)
		{
			this.addedTs = addedTs;
			this.list = list;
		}
	}
	
	public void setMaxShowCacheAge(long maxShowCacheAge)
	{
		this.maxShowCacheAge = maxShowCacheAge;
	}

	public long getMaxShowCacheAge()
	{
		return maxShowCacheAge;
	}

	public void setMaxListCacheAge(long maxListCacheAge)
	{
		this.maxListCacheAge = maxListCacheAge;
	}

	public long getMaxListCacheAge()
	{
		return maxListCacheAge;
	}
}
