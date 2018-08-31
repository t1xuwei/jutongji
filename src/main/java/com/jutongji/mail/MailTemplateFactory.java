package com.jutongji.mail;

import com.jutongji.config.mail.MailCfg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MailTemplateFactory
{
    /**
     * 重设密码Mail模板
     */
    private static String resetMailTpl;

    public static String getResetMailTpl()
    {
        if (resetMailTpl == null || resetMailTpl.length() == 0)
        {
            try
            {
                String tplPath = MailCfg.getInstance().getResetTemplateFile();
                FileInputStream fin = new FileInputStream(new File(tplPath));
                BufferedReader in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                String s;
                resetMailTpl = "";
                while ((s = in.readLine()) != null)
                {
                    resetMailTpl += s + "\n";
                }
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return resetMailTpl;
    }

    /**
     * 绑定邮箱Mail模板
     */
    private static String bindingMailTpl;

    public static String getBindingMailTpl()
    {
        if (bindingMailTpl == null || bindingMailTpl.length() == 0)
        {
            try
            {
                String tplPath = MailCfg.getInstance().getBindingTemplateFile();
                FileInputStream fin = new FileInputStream(new File(tplPath));
                BufferedReader in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                String s;
                bindingMailTpl = "";
                while ((s = in.readLine()) != null)
                {
                    bindingMailTpl += s + "\n";
                }
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return bindingMailTpl;
    }

    /**
     * 激活用户账号Mail模板
     */
    private static String registMailTpl;

    public static String getRegistMailTpl()
    {
        if (registMailTpl == null || registMailTpl.length() == 0)
        {
            try
            {
                String tplPath = MailCfg.getInstance().getRegistTemplateFile();
                FileInputStream fin = new FileInputStream(new File(tplPath));
                BufferedReader in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                String s;
                registMailTpl = "";
                while ((s = in.readLine()) != null)
                {
                    registMailTpl += s + "\n";
                }
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return registMailTpl;
    }

    /**
     * 快速激活用户账号Mail模板
     */
    private static String quickRegistMailTpl;

    public static String getQuickRegistMailTpl()
    {
        if (quickRegistMailTpl == null || quickRegistMailTpl.length() == 0)
        {
            try
            {
                String tplPath = MailCfg.getInstance().getQuickRegistTemplateFile();
                FileInputStream fin = new FileInputStream(new File(tplPath));
                BufferedReader in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                String s;
                quickRegistMailTpl = "";
                while ((s = in.readLine()) != null)
                {
                    quickRegistMailTpl += s + "\n";
                }
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return quickRegistMailTpl;
    }

}
