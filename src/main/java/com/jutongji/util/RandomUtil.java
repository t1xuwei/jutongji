package com.jutongji.util;

import java.util.Random;

public class RandomUtil
{
    private static final int    SHUFFLE_TIMES = 3;
    
    private static final char[] DEFAULT_CHARS = new char[]
    {
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
    };
    
    private static final char[] NUMBER_CHARS = new char[]
    {
        '0','1','2','3','4','5','6','7','8','9',
    };
    
    private static final char[] NONEZERO_NUMBER_CHARS = new char[]
    {
        '1','2','3','4','5','6','7','8','9',
    };
    
    public static int genRandomNumber(int begin, int end)
    {
        int step   = Math.abs(end - begin);
        int random = new Random(System.nanoTime()).nextInt(step);
        
        return Math.min(begin, end) + random;
    }
    
    public static String genRandomString(int count)
    {
        return genRandomString(count, DEFAULT_CHARS);
    }
    
    public static String genRandomString(int count, char[] elements)
    {
        StringBuffer sb = new StringBuffer();
        
        int len = elements.length - 1;
        for(int i = 0; i < count; i++)
        {
            sb.append(elements[new Random(System.nanoTime()).nextInt(len)]);
        }
        
        return sb.toString();
    }
    
    private static char[] shuffle(char[] elements, int times)
    {
        int len = elements.length;
        //不改变原有char数组
        char[] data = new char[len];
        System.arraycopy(elements, 0, data, 0, len);
        
        for(int i = 0; i < times; i++)
        {
            for(int j = 0; j < len; j++)
            {
                int index = new Random(System.nanoTime()).nextInt(len);
                
                char c = data[0];
                data[0] = data[index];
                data[index] = c;
            }
        }
        
        return data;
    }
    
    public static String genUnrepeatedRandomString(int count, char[] elements)
    {
       char[] data = shuffle(elements, SHUFFLE_TIMES);
       
       char[] result = new char[Math.min(count, data.length)];
       System.arraycopy(data, 0, result, 0, result.length);
       
       return new String(result);
    }
    
    public static String genUnrepeatedRandomString(int count)
    {
        return genUnrepeatedRandomString(count, DEFAULT_CHARS);
    }
    
    public static String genRandomNumberString(int numberCount)
    {
        return genRandomString(1, NONEZERO_NUMBER_CHARS) + genRandomString(numberCount - 1, NUMBER_CHARS);
    }
    
    public static void main(String[] args)
    {
        try
        {
            System.out.println(genRandomNumber(0, 199999));
            
            System.out.println(genRandomString(30));
            
            System.out.println(genUnrepeatedRandomString(30));
            System.out.println(genUnrepeatedRandomString(100));
            
            System.out.println(genRandomNumberString(4));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
