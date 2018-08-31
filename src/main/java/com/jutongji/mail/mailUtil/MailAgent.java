/**
 * @(#)MailAgent.java
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
public interface MailAgent
{
    public void send(String receiver, String subject, String content) throws Exception;
    public void send(String receiver, String subject, String content, Map<String, String> attachments) throws Exception;
}


/**
 * $Log: MailAgent.java,v $
 * 
 * @version 1.0 2012-8-14 
 *
 */