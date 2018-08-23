package com.jutongji.session;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContext
{
    private static ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();

    public static void addSession(HttpSession session)
    {
        if (session != null)
        {
            sessionMap.put(session.getId(), session);
        }
    }

    public static void delSession(HttpSession session)
    {
        if (session != null)
        {
            sessionMap.remove(session.getId());
        }
    }

    public static HttpSession getSession(String session_id)
    {
        if (session_id == null)
            return null;

        return (HttpSession) sessionMap.get(session_id);
    }
}
