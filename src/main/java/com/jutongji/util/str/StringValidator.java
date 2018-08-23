/**
 * @(#)StringValidator.java
 *
 * @author xuji
 * @version 1.0 2012-8-3
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.util.str;
/**
 * 
 * Purpose:
 * 
 * @see     
 * @since   1.1.0
 */
public class StringValidator 
{
    
    /**
     * 中文判断
     * @param str
     * @return
     */
    public static boolean isChinese(String str)
    {
        String regExp = "[\u4e00-\u9fa5]+";
        return str.matches(regExp);
    }
    
    /**
     * 字母判断
     * @param str
     * @return
     */
    public static boolean isLetter(String str)
    {
        String regExp = "^[A-Za-z]+$";
        return str.matches(regExp);
    }
    
    /**
     * 数字判断
     * @param str
     * @return
     */
    public static boolean isNumber(String str)
    {
        String regExp = "[0-9]+";
        return str.matches(regExp);
    }
    
    public static void main(String[] args) 
    {
        String str = "0123";
        str = "12你";
        
        
        System.out.println(str+" is Chinese:"+isChinese(str));
    }
    
}


/**
 * $Log: StringValidator.java,v $
 * 
 * @version 1.0 2012-8-3 
 *
 */