/**
 * @(#)IMailScheduler.java
 *
 * @author xuji
 * @version 1.0 2012-8-14
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.mail.mailUtil;


/**
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
 */
public interface IMailScheduler 
{
    public boolean start();
    public void stop(long timeout);
    public boolean isActive();
    public boolean putMail(IMailEntry entry);

    public long getQueueSize();
    public long getLastUpdateTime();
}


/**
 * $Log: IMailScheduler.java,v $
 * 
 * @version 1.0 2012-8-14 
 *
 */