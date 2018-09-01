/**
 * @(#)MailEntry.java
 *
 * @author xuji
 * @version 1.0 2012-8-14
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.mail.mailUtil;

import com.jutongji.config.mail.MailCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
 */
public abstract class MailEntry implements IMailEntry
{
    int retryLimit = 1;

    public Map<String, String> getAttachments() 
    {
        return null;
    }

    public int getRetryLimit() 
    {
        return retryLimit;
    }

    public void setRetryLimit(int retryLimit) 
    {
        this.retryLimit = retryLimit;
    }

    public void decreaseRetryLimit() 
    {
        retryLimit--;
    }
    
    public void increaseRetryLimit()
    {
        retryLimit++;
    }
}

/**
 * $Log: MailEntry.java,v $
 * 
 * @version 1.0 2012-8-14 
 *
 */