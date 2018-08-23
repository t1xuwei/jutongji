/**
 * @(#)StringUtils.java
 *
 * @author xuji
 * @version 1.0 2012-8-3
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.util.str;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Purpose:
 * 
 * @see     
 * @since   1.1.0
 */
public class StringUtils
{
    public static String deleteWhitespace(String text)
    {
        return org.apache.commons.lang.StringUtils.deleteWhitespace(text);
    }
    
    public static String unescapeHtml(String text)
    {
        String result = text.replaceAll("&nbsp;", " ");
        try
        {
            result = StringEscapeUtils.unescapeHtml(result);
        }
        catch(Exception e)
        {
            result = org.apache.commons.lang.StringUtils.strip(result);
        }
        result = result.replace("\t", "");
        result = result.replace("\r", "");
        result = result.replace("\n", "");
        
        return result;
    }
    
    public static String deleteHtmlWhitespace(String text)
    {
        String result = text.replaceAll("&nbsp;", " ");
        try
        {
            result = StringEscapeUtils.unescapeHtml(result);
        }
        catch(Exception e)
        {
            
        }
        result = deleteWhitespace(result);

        return result;
    }
    
    
    public static String removeHtmlAnnote(String html)
    {
        if(null != html && !"".equals(html))
        {
            return html.replaceAll("<!--", "").replaceAll("-->", "");
        }
        
        return html;
    }

    
    public static String replaceChars(String text, Map<String, String> replaceMap)
    {
        char[] charArray = text.toCharArray();
        
        StringBuffer sb = new StringBuffer();
        
        for (char c : charArray) 
        {
            String element = replaceMap.get(String.valueOf(c)); 
            if(null != element)
            {
                sb.append(element);
            }
            else
            {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }

    /**
     * 生成随机数
     * @param sLen
     * @return String
     */
    public static String getRandomString(int sLen) 
    {
        String base = "1234567890abcdefghijklmnopqrstuvwxyz";
        String temp = "";
        for (int i = 0; i < sLen; i++)
        {
            int p = (int)(Math.random() * 37);
            if (p > 35) 
            {
                p = 35;
            }
            temp += base.substring(p, p + 1);
        }
        
        return temp;
    }

    /**
     * obj 2 str
     * @param obj
     * @return "" if obj is null
     */
    public static String valueOf(Object obj)
    {
        if (null == obj)
        {
            return "";
        }
        
        return obj.toString();
    }
    
    
    public static String ary2Str(Object[] values)
    {
        return ary2Str(values, ',');
    }
    
    public static String ary2Str(Object[] values, char sep)
    {
        return org.apache.commons.lang.StringUtils.join(values, sep);
    }
    
    public static String ary2Str(int[] values)
    {
        return ary2Str(values, ',');
    }
    
    public static String ary2Str(int[] values, char sep)
    {
        if (null != values)
        {
            Object[] elements = new Integer[values.length];
            for(int i = 0; i < values.length; i++)
            {
                elements[i] = Integer.valueOf(values[i]);
            }
            return ary2Str(elements, sep);
        }
        
        return null;
    }
    
    public static String ary2Str(long[] values)
    {
        return ary2Str(values, ',');
    }
    
    public static String ary2Str(long[] values, char sep)
    {
        if (null != values)
        {
            Object[] elements = new Long[values.length];
            for(int i = 0; i < values.length; i++)
            {
                elements[i] = Long.valueOf(values[i]);
            }
            return ary2Str(elements, sep);
        }
        
        return null;
    }
    
    public static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为乱码
     * 
     * @param strName
     * @return
     */
    public static boolean isMessyCode(String strName)
    {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++)
        {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c))
            {
                if (!isChinese(c))
                {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
    /**
     * 判断是否为手机号
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }
    
    public static void main(String[] args)
    {
        String test = "中\r\n国((不缺乏)程'序'员";
        System.out.println(replaceENcharWithCNchar(test));
        
    }
    public static String replaceENcharWithCNchar(String str){
        str = str.replaceAll("\\(", "（");
        str = str.replaceAll("\\)", "）");
        str = str.replaceAll("'", "‘");
        str = str.replaceAll("\\r\\n|\\r|\\n", "<br/>");
        
        return str;
    }
}
/**
 * $Log: StringUtils.java,v $
 * 
 * @version 1.0 2012-8-3 
 *
 */