package com.jutongji.cache;

import java.util.concurrent.TimeUnit;

public class CacheFactory
{
    private CacheFactory()
    {
    }
    
    private static class CacheFactoryHolder
    {
        private static final CacheFactory instance = new CacheFactory();
    }
    
    public static CacheFactory getInstance()
    {
        return CacheFactoryHolder.instance;
    }
    
    public <K,V> AbstractCache<K,V> newCache(long cacheDuration, TimeUnit timeUnit, long maxCacheSize)
    {
        return new GuavaCacheImpl<K, V>(cacheDuration, timeUnit, maxCacheSize);
    }
}
