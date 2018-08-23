/**
 * @(#)UserConditionCache.java
 *
 * @author huawei
 * @version 1.0 2015-4-17
 *
 * Copyright (C) 2012,2015 , PING' AN, Inc.
 */
package com.jutongji.session;

import com.jutongji.cache.AbstractCache;
import com.jutongji.cache.CacheFactory;


import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
public class UserSessionCache
{
    private AbstractCache<String,UserSession> cache;
    private UserSessionCache()
    {
        cache = CacheFactory.getInstance().newCache(30 * 60, TimeUnit.SECONDS, 100000);
    }

    public static UserSessionCache getInstance()
    {
        return UserSessionCacheHolder.instance;
    }

    private static class UserSessionCacheHolder
    {
        private static UserSessionCache instance = new UserSessionCache();
    }


    public UserSession getUserSession(HttpSession session) throws Exception
    {
        if(!checkSession(session))
        {
            return null;
        }
        return cache.get(session.getId());
    }

    public void removeUserSession(HttpSession session) throws Exception
    {
        if(!checkSession(session))
        {
            return;
        }
        cache.invalidCache(session.getId());
    }
    
    public boolean isSessionInCache(HttpSession session) throws Exception
    {
        if(!checkSession(session))
        {
            return false;
        }
        
        return cache.isInCache(session.getId());
    }
    
    private boolean checkSession(HttpSession session)
    {
        return session != null;
    }

    public void cacheUserSession(HttpSession session, UserSession userSession) throws Exception
    {
        if(!checkSession(session))
        {
            return;
        }
        cache.cache(session.getId(), userSession);
    }
}

/**
 * $Log: UserConditionCache.java,v $
 * 
 * @version 1.0 2015-4-17
 */
