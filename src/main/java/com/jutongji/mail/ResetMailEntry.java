package com.jutongji.mail;

import com.jutongji.config.mail.MailCfg;
import com.jutongji.mail.mailUtil.MailEntry;


public class ResetMailEntry extends MailEntry
{
    private String receiver;

    private String resetUrl;

    public ResetMailEntry(String receiver, String resetUrl)
    {
        super();
        this.receiver = receiver;
        this.resetUrl = resetUrl;
    }

    public String getContent()
    {
        String emailTemplate = MailTemplateFactory.getResetMailTpl();
        String emailContent = emailTemplate.replaceAll("#resetUrl#", getResetUrl());
        emailContent = emailContent.replaceAll("#userName#", this.receiver);

        return emailContent;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public String getSubject()
    {
        return "重设" + MailCfg.getInstance().templateTitle + "‏密码";
    }

    public String getResetUrl()
    {
        return resetUrl;
    }

    public void setResetUrl(String resetUrl)
    {
        this.resetUrl = resetUrl;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }
}