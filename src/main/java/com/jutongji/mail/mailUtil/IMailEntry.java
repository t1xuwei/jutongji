/**
 * @(#)IMailEntry.java
 *
 * @author xuji
 * @version 1.0 2012-8-14
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.mail.mailUtil;

import java.util.Map;

/**
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
 */
public interface IMailEntry
{
    public String getReceiver();
    public String getSubject();
    public String getContent();
    public Map<String, String> getAttachments();

    public int getRetryLimit();
    public void decreaseRetryLimit();
    public void increaseRetryLimit();
}



/**
 * $Log: IMailEntry.java,v $
 * 
 * @version 1.0 2012-8-14 
 *
 */