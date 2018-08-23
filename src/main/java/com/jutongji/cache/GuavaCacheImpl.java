package com.jutongji.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class GuavaCacheImpl<K, V> extends AbstractCache<K, V>
{
    private Cache<K, V> cache;

    public GuavaCacheImpl(long cacheDuration, TimeUnit timeUnit, long maxCacheSize)
    {
        super(cacheDuration, timeUnit, maxCacheSize);
        this.cache = recreateCache();
    }

    @Override
    public boolean cache(K key, V value) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        cache.put(key, value);
        return true;
    }

    @Override
    public V getAndUpdateCache(final K key) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        try
        {
            return cache.get(key, new Callable<V>()
            {

                @Override
                public V call() throws Exception
                {
                    V v = cache.getIfPresent(key);
                    if (null == v)
                    {
                        return null;
                    }

                    cache.invalidate(key);
                    cache.put(key, v);
                    return v;
                }
            });
        }
        catch (Exception e)
        {
        }

        return null;
    }

    @Override
    public V get(final K key) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        try
        {
            return cache.getIfPresent(key);
        }
        catch (Exception e)
        {
        }

        return null;
    }

    @Override
    public boolean invalidCache(K key) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        cache.invalidate(key);
        return false;
    }

    @Override
    public boolean isInCache(final K key) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        try
        {
            return cache.getIfPresent(key) != null;
        }

        catch (Exception e)
        {
        }

        return false;
    }

    @Override
    public Map<K, V> getAllCaches() throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        return cache.asMap();
    }

    @Override
    public boolean cacheAll(Map<K, V> cacheData) throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        cache.putAll(cacheData);
        return true;
    }

    /**
     * 根据配置创建缓存
     * 
     * @return
     */
    public Cache<K, V> recreateCache()
    {
        Cache<K, V> newCache = CacheBuilder.newBuilder().expireAfterWrite(cacheDuration, timeUnite).maximumSize(maxCacheSize).build();
        return newCache;
    }

    protected boolean checkCache()
    {
        return cache != null;
    }

    @Override
    public long size() throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        return cache.size();
    }

    @Override
    public boolean invalidAllCache() throws Exception
    {
        if (!checkCache())
        {
            throw new Exception("缓存不可用");
        }

        cache.invalidateAll();

        return false;
    }
}
