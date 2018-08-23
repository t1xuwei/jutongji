package com.jutongji.cache;

import java.util.Map;

public interface ICache<K,V>
{
    /**
     * 放入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean cache(K key, V value) throws Exception;
    
    public boolean cacheAll(Map<K, V> cacheData) throws Exception;
    
    /**
     * 移除缓存
     * @param key
     * @return
     */
    public boolean invalidCache(K key) throws Exception;
    
    /**
     * 移除所有缓存
     * @return
     */
    public boolean invalidAllCache() throws Exception;
    
    /**
     * 是否在缓存中存在
     * @param key
     * @return
     */
    public boolean isInCache(K key) throws Exception;
    
    /**
     * 获取缓存的之后重新计算缓存时间
     * @param key
     * @return
     */
    public V getAndUpdateCache(K key) throws Exception;
    
    /**
     * 获取缓存并且不会重新计算缓存时间
     * @param key
     * @return
     * @throws Exception
     */
    public V get(final K key) throws Exception;
    
    /**
     * 获取缓存中所有数据
     * @return
     * @throws Exception
     */
    public Map<K, V> getAllCaches() throws Exception;
    
    /**
     * 获取当前缓存中条数
     * @return
     * @throws Exception
     */
    public long size() throws Exception;
}
