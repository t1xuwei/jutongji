/**
 * @(#)MailSchedulerImpl.java
 *
 * @author xuji
 * @version 1.0 2012-8-14
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.mail.mailUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 
 * Purpose:
 * 
 * @see     
 * @since   1.1.0
 */
public abstract class MailSchedulerImpl implements IMailScheduler
{
    private static Logger logger = LoggerFactory.getLogger(MailSchedulerImpl.class);
    
    

    private ThreadPoolExecutor threadPool;
    private LinkedBlockingQueue<Runnable> taskQueue;
    
    private int coreThreadSize              = 1;
    private int maxThreadSize               = 1;
    private int maxTask                     = 10000;
    private int rejectedTime                = 10000;
    private int sendInterval                = 6000;
    
        
    public MailSchedulerImpl()
    {
        
    }

    
    @Override
    public synchronized boolean isActive()
    {
        return (null != threadPool) && (!threadPool.isTerminated()) && (!threadPool.isShutdown());
    }
    
    @Override
    public synchronized boolean start()
    {
        if (isActive())
        {
            logger.info("MailSchedulerImpl::start::Mail Scheduler已启动! ");
            return false;
        }
        
        if (null == getMailAgent())
        {
            logger.error("MailSchedulerImpl::start::未设置MailAgent!");
            return false;
        }
        
        taskQueue   = new LinkedBlockingQueue<Runnable>(maxTask);
        threadPool  = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, 3, TimeUnit.SECONDS, taskQueue);
        
        threadPool.setRejectedExecutionHandler(new RejectedExecutionHandler()
        {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
            {
                try
                {
                    logger.info("MailSchedulerImpl::start::reject task! please wait.......!");
                    r.run();
                    Thread.sleep(rejectedTime);
                }
                catch(Exception e)
                {
                    logger.error("MailSchedulerImpl::start::run rejected task fail!", e);
                }
            }
            
        });
        
        logger.info("MailSchedulerImpl::start::启动MailScheduler成功!");
        return true;
    }

    @Override
    public synchronized void stop(long timeout)
    {
        try
        {
            if (isActive())
            {
                threadPool.shutdown();
                if (!threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS))
                {
                    threadPool.shutdownNow();
                    if (!threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS))
                    {
                        logger.error("MailSchedulerImpl::stop::停止MailScheduler失败!");
                    }
                }
            }
        }
        catch(Exception e)
        {
            logger.error("MailSchedulerImpl::stop::停止MailScheduler发生异常!", e);
        }
    }

    @Override
    public boolean putMail(IMailEntry entry)
    {
        if (!isActive())
        {
            logger.error("MailSchedulerImpl::putMail::邮件服务未激活，开始启动邮件服务!");
            start();
        }
        
        threadPool.execute(new MailTask(entry));
        return true;
    }

    @Override
    public long getQueueSize()
    {
        return taskQueue.size();
    }

    @Override
    public long getLastUpdateTime()
    {
        return 0;
    }

    
    /**
     * @return the coreThreadSize
     */
    public int getCoreThreadSize()
    {
        return coreThreadSize;
    }

    /**
     * @param coreThreadSize the coreThreadSize to set
     */
    public void setCoreThreadSize(int coreThreadSize)
    {
        this.coreThreadSize = coreThreadSize;
    }

    /**
     * @return the maxThreadSize
     */
    public int getMaxThreadSize()
    {
        return maxThreadSize;
    }

    /**
     * @param maxThreadSize the maxThreadSize to set
     */
    public void setMaxThreadSize(int maxThreadSize)
    {
        this.maxThreadSize = maxThreadSize;
    }

    /**
     * @return the maxTask
     */
    public int getMaxTask()
    {
        return maxTask;
    }

    /**
     * @param maxTask the maxTask to set
     */
    public void setMaxTask(int maxTask)
    {
        this.maxTask = maxTask;
    }

    /**
     * @return the rejectedTime
     */
    public int getRejectedTime()
    {
        return rejectedTime;
    }

    /**
     * @param rejectedTime the rejectedTime to set
     */
    public void setRejectedTime(int rejectedTime)
    {
        this.rejectedTime = rejectedTime;
    }
    
    
    
    public int getSendInterval()
    {
        return sendInterval;
    }


    public void setSendInterval(int sendInterval)
    {
        this.sendInterval = sendInterval;
    }


    abstract public MailAgent getMailAgent();

    private class MailTask implements Runnable
    {
        private IMailEntry mail;
        
        public MailTask(IMailEntry mail)
        {
            this.mail = mail;
        }

        @Override
        public void run()
        {
            logger.info("MailTask::run::开始执行任务!");
            if (null == mail)
            {
                logger.error("MailTask::run::mail is null, 发送邮件失败!");
                return;
            }
            
            logger.info(String.format("MailTask::run::任务[%s]开始执行!", mail.getReceiver()));
            
            MailAgent agent = MailSchedulerImpl.this.getMailAgent();
            if (null == agent)
            {
                logger.error("MailTask::run::MailAgent is null, 发送邮件失败!");
                return;
            }
            
            String receiver = mail.getReceiver();
            String subject  = mail.getSubject();
            String content = mail.getContent();
            Map<String, String> attachments = mail.getAttachments();
            
            try
            {
                if (null != receiver && null != subject && null != content)
                {
                    agent.send(receiver, subject, content, attachments);
                    logger.info("MailTask::run::{}", String.format("发送主题[%s]邮件给%s", subject, receiver));
                    
                    //增加sleep时间抑制发送速度
                    Thread.sleep(getSendInterval());
                }
            }
            catch(Exception e)
            {
                if (mail.getRetryLimit() > 0)
                {
                    logger.info(String.format("MailTask::run::发送主题[%s]邮件给%s失败 , 尝试重发!",  subject, receiver), e);
                    
                    mail.decreaseRetryLimit();
                    MailSchedulerImpl.this.putMail(mail);
                    
                }
                else
                {
                    logger.info(String.format("MailTask::run::发送主题[%s]邮件给%s失败次数达到上限，丢弃!",  subject, receiver), e);
                }
            }
        }
    }
}


/**
 * $Log: MailSchedulerImpl.java,v $
 * 
 * @version 1.0 2012-8-14 
 *
 */