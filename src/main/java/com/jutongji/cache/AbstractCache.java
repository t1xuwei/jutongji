package com.jutongji.cache;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCache<K, V> implements ICache<K, V>
{
    protected long cacheDuration = 1;

    protected TimeUnit timeUnite = TimeUnit.SECONDS;

    protected long maxCacheSize = Integer.MAX_VALUE;

    public AbstractCache(long cacheDuration, TimeUnit timeUnit, long maxCacheSize)
    {
        this.cacheDuration = cacheDuration;
        this.timeUnite = timeUnit;
        this.maxCacheSize = maxCacheSize;
    }

    public long getCacheDuration()
    {
        return cacheDuration;
    }

    public void setCacheDuration(long cacheDuration)
    {
        this.cacheDuration = cacheDuration;
    }

    public TimeUnit getTimeUnite()
    {
        return timeUnite;
    }

    public void setTimeUnite(TimeUnit timeUnite)
    {
        this.timeUnite = timeUnite;
    }

    public long getMaxCacheSize()
    {
        return maxCacheSize;
    }

    public void setMaxCacheSize(long maxCacheSize)
    {
        this.maxCacheSize = maxCacheSize;
    }
}
