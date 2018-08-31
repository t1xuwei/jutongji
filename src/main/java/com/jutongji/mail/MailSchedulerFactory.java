package com.jutongji.mail;

import com.jutongji.config.mail.MailCfg;
import com.jutongji.mail.mailUtil.IMailScheduler;
import com.jutongji.mail.mailUtil.MailAgent;
import com.jutongji.mail.mailUtil.MailSchedulerImpl;
import com.jutongji.mail.mailUtil.SmtpAgent;

public class MailSchedulerFactory
{
    public static synchronized IMailScheduler getMailScheduler()
    {
        return MyMailScheduler.getInstance();
    }

    /*
     * v1.2.0 xuji 2013-07-18 解决邮件不稳定问题 -{{ public static IMailScheduler
     * getSMTPMailScheduler() { MailSchedulerImpl scheduler =
     * MailSchedulerImpl.getInstance(); scheduler.setMailAgent(new
     * SmtpAgent(MailCfg.getInstance().mailHost, MailCfg.getInstance().mailFrom,
     * MailCfg .getInstance().mailSenderPassword,
     * MailCfg.getInstance().mailName)); return scheduler; }
     */
    // }}

    private static class MyMailScheduler extends MailSchedulerImpl
    {
        private static IMailScheduler instance = null;

        public static IMailScheduler getInstance()
        {
            if (null == instance)
            {
                instance = new MyMailScheduler();
            }

            return instance;
        }

        private MyMailScheduler()
        {

        }

        @Override
        public MailAgent getMailAgent()
        {
            return new SmtpAgent(MailCfg.getInstance().mailHost, MailCfg.getInstance().mailSender,
                    MailCfg.getInstance().mailSenderPassword, MailCfg.getInstance().mailFrom,
                    MailCfg.getInstance().mailName);
        }

    }
}
