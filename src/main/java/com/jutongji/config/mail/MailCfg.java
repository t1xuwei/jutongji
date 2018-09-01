package com.jutongji.config.mail;

import com.jutongji.util.SpringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jutongji.mail")
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

    public String mailName;

    public static MailCfg getInstance() {
        return (MailCfg) SpringUtil.getBean("mailCfg");
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
        return registTemplateFile;
    }

    public String getQuickRegistTemplateFile()
    {
        return "邮件模板地址";
    }

    public String getRegistUrl()
    {
        return String.format("%s", registUrl);
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    public void setMailSenderPassword(String mailSenderPassword) {
        this.mailSenderPassword = mailSenderPassword;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setResetTemplateFile(String resetTemplateFile) {
        this.resetTemplateFile = resetTemplateFile;
    }

    public void setResetUrl(String resetUrl) {
        this.resetUrl = resetUrl;
    }

    public void setBindingTemplateFile(String bindingTemplateFile) {
        this.bindingTemplateFile = bindingTemplateFile;
    }

    public void setBindingUrl(String bindingUrl) {
        this.bindingUrl = bindingUrl;
    }

    public void setRegistTemplateFile(String registTemplateFile) {
        this.registTemplateFile = registTemplateFile;
    }

    public void setQuickRegistTemplateFile(String quickRegistTemplateFile) {
        this.quickRegistTemplateFile = quickRegistTemplateFile;
    }

    public void setRegistUrl(String registUrl) {
        this.registUrl = registUrl;
    }

    public void setQuickRegistUrl(String quickRegistUrl) {
        this.quickRegistUrl = quickRegistUrl;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName;
    }
}
