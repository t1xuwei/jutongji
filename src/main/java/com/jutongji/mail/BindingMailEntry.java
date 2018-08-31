package com.jutongji.mail;

import com.jutongji.config.mail.MailCfg;
import com.jutongji.mail.mailUtil.MailEntry;

public class BindingMailEntry extends MailEntry
{
    private String receiver;

    private String bindingUrl;

    public String getBindingUrl()
    {
        return bindingUrl;
    }

    public void setBindingUrl(String bindingUrl)
    {
        this.bindingUrl = bindingUrl;
    }

    public BindingMailEntry(String receiver, String bindingUrl)
    {
        super();
        this.receiver = receiver;
        this.bindingUrl = bindingUrl;
    }

    public String getContent()
    {
        String emailTemplate = MailTemplateFactory.getBindingMailTpl();
        String emailContent = emailTemplate.replaceAll("#bindingUrl#", getBindingUrl());
        emailContent = emailContent.replaceAll("#userName#", this.receiver);


        return emailContent;
    }

    public String getSubject()
    {
        return "绑定" + MailCfg.getInstance().templateTitle + "邮箱";
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