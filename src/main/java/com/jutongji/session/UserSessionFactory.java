package com.jutongji.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSessionFactory
{
    static public final String SESSION_NAME = "user";

    static public final String COOKIE_NICK = "U_NICK";

    static public final String COOKIE_USERNAME = "U_USERNAME";

    static public final String JSESSIONID = "jsessionid";
    
    static public boolean useCache = true;
    
    static protected String getNickInCookies(Cookie[] cookies)
    {
        if (cookies == null)
        {
            return "";
        }
        for (int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals(COOKIE_NICK))
            {
                return cookies[i].getValue();
            }
        }

        return "游客";
    }

    static protected String getUserNameInCookies(Cookie[] cookies)
    {
        if (cookies == null)
        {
            return "";
        }
        for (int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals(COOKIE_USERNAME))
            {
                return cookies[i].getValue();
            }
        }

        return "";
    }
    
    static private UserSession getUserSessionFromCache(HttpSession session) throws Exception
    {
        return UserSessionCache.getInstance().getUserSession(session);
    }
    static private void updateUserSessionInCache(HttpSession session, UserSession userSession) throws Exception
    {
        if(useCache)
        {
            UserSessionCache.getInstance().cacheUserSession(session, userSession);
        }
    }
    static private void clearUserSessionInCache(HttpSession session) throws Exception
    {
        if(useCache)
        {
            UserSessionCache.getInstance().removeUserSession(session);
        }
    }

    static public UserSession getUserSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        
        return getUserSessionByJsessionId(session, request.getParameter(JSESSIONID));
        
        /*
         * if (us == null) { //从Cookie中获取userName和Nick，构造Guest用户的Session
         * Cookie[] cookies = request.getCookies(); us = new UserSession();
         * us.setUserName(getUserNameInCookies(cookies));
         * us.setNick(getNickInCookies(cookies));
         * //session.setAttribute(SESSION_NAME, us); }
         */
    }
    
    static public void updateUserSession(HttpServletRequest request, UserSession us)
    {
        HttpSession session = request.getSession();
        updateUserSession(session, us);
    }
    
    static public void clearUserSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        clearUserSession(session);
    }

    static public UserSession getUserSession(HttpSession session)
    {
        return getUserSessionByJsessionId(session, session.getId());
    }

    static public void updateUserSession(HttpSession session, UserSession us)
    {
        session.setAttribute(SESSION_NAME, us);
        
        //更新session缓存
        try
        {
            updateUserSessionInCache(session, us);
        }
        catch(Exception e)
        {
        }
    }

    static public UserSession getUserSessionByJsessionId(HttpSession session, String id)
    {
        //从缓存中取UserSession
        if(useCache)
        {
            try
            {
                return getUserSessionFromCache(session);
            }
            catch(Exception e)
            {
            }
        }
        
        UserSession us = (UserSession) session.getAttribute(SESSION_NAME);
        if (us == null && id != null && SessionContext.getSession(id) != null)
        {
            us = (UserSession) SessionContext.getSession(id).getAttribute(SESSION_NAME);
        }
        
        //再次判断缓存中是否存在
        try
        {
            if(useCache)
            {
                if(!UserSessionCache.getInstance().isSessionInCache(session))
                {
                    //新增session缓存
                    updateUserSessionInCache(session, us);
                }
            }//end if(useCache)
        }
        catch(Exception e)
        {
        }
        
        return us;
    }

    static public void clearUserSession(HttpSession session)
    {
        session.invalidate();
        
        //移除session缓存
        try
        {
            clearUserSessionInCache(session);
        }
        catch(Exception e)
        {
        }
    }
}
