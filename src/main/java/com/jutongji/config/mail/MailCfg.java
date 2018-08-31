package com.jutongji.config.mail;

public class MailCfg
{
    private static final long serialVersionUID = 01L;

    public String mailHost;

    public String mailSender;

    public String mailSenderPassword;

    public String mailFrom;

    public String resetTemplateFile;

    public String resetUrl;

    public String bindingTemplateFile;

    public String bindingUrl;

    public String registTemplateFile;

    public String quickRegistTemplateFile;

    public String registUrl;

    public String quickRegistUrl;

    public String templateTitle = "邮件标题";

    private static MailCfg instance = new MailCfg();
    public String mailName;


    public static MailCfg getInstance()
    {
        return instance;
    }


    public void verifyConfig() throws Exception
    {

    }

    public String getQuickRegistUrl()
    {
        return quickRegistUrl;
    }

    public String getResetTemplateFile()
    {
        return "邮件模板地址";
    }

    public String getResetUrl()
    {
        return String.format("%s", resetUrl);
    }

    public String getBindingTemplateFile()
    {
        return "邮件模板地址";
    }

    public String getBindingUrl()
    {
        return String.format("%s", bindingUrl);
    }

    public String getRegistTemplateFile()
    {
        return "邮件模板地址";
    }

    public String getQuickRegistTemplateFile()
    {
        return "邮件模板地址";
    }

    public String getRegistUrl()
    {
        return String.format("%s", registUrl);
    }
}
