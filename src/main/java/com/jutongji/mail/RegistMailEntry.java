package com.jutongji.mail;

import com.jutongji.config.mail.MailCfg;
import com.jutongji.mail.mailUtil.MailEntry;

public class RegistMailEntry extends MailEntry
{
    private String receiver;

    private String registUrl;

    private String userCsName;

    private String userCsQQ;

    public String getUserCsQQ()
    {
        return userCsQQ;
    }

    public void setUserCsQQ(String userCsQQ)
    {
        this.userCsQQ = userCsQQ;
    }

    public String getUserCsName()
    {
        return userCsName;
    }

    public void setUserCsName(String userCsName)
    {
        this.userCsName = userCsName;
    }

    public String getRegistUrl()
    {
        return registUrl;
    }

    public void setRegistUrl(String registUrl)
    {
        this.registUrl = registUrl;
    }

    public RegistMailEntry(String receiver, String registUrl)
    {
        super();
        this.receiver = receiver;
        this.registUrl = registUrl;
    }

    public String getContent()
    {
        String emailTemplate = MailTemplateFactory.getRegistMailTpl();
        String emailContent = emailTemplate.replaceAll("#registUrl#", getRegistUrl());
        emailContent = emailContent.replaceAll("#userName#", this.receiver);


        return emailContent;
    }

    public String getSubject()
    {
        return "欢迎注册" + MailCfg.getInstance().templateTitle;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

}