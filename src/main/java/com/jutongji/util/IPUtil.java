package com.jutongji.util;

public class IPUtil
{
    public static long ip2long(String ip) throws Exception
    {
        long result = 0;
        
        String[] nums = ip.split("\\.");
        
        if (nums.length != 4)
        {
            throw new Exception("ip格式不正确!");
        }
        
        
        for(int i = 0; i < 4; i++)
        {
            int value = Integer.parseInt(nums[i]);
            if (value > 255 || value < 0)
            {
                throw new Exception("ip格式不正确！");
            }
            
            result = result | (value << ((3 - i) * 8));
        }
        
        return result & (0x00000000FFFFFFFFL);
    }
    
    
    public static String long2ip(long address)
    {
        long d = address & 0xFF;
        long c = (address >> 8)  & 0xFF;
        long b = (address >> 16) & 0xFF;
        long a = (address >> 24) & 0xFF;
        
        return String.format("%d.%d.%d.%d", a, b, c, d);
    }
    
    public static boolean isPrivateAddress(String ip) throws Exception
    {
        int[]    nums   = new int[4];
        String[] values = ip.split("\\.");
        
        if (values.length != 4)
        {
            throw new Exception("ip格式不正确!");
        }
        
        for(int i = 0; i < 4; i++)
        {
            nums[i] = Integer.parseInt(values[i]);
        }

        if (nums[0] == 127)
        {
            return true;
        }
        
        if (nums[0] == 10)
        {
            return true;
        }
        
        if ((nums[0] == 172) && ( (nums[1] & 0xF0) == 16))
        {
            return true;
        }
        
        if ((nums[0] == 192) && (nums[1] == 168))
        {
            return true;
        }
        
        return false;
    }
    
    public static void main(String[] args)
    {
        try
        {
            System.out.println(long2ip(3232263484L));
            System.out.println(ip2long("192.168.109.60"));
            
            System.out.println(isPrivateAddress("127.0.0.1"));
            System.out.println(isPrivateAddress("10.0.0.1"));
            System.out.println(isPrivateAddress("172.16.0.1"));
            System.out.println(isPrivateAddress("172.32.0.1"));
            System.out.println(isPrivateAddress("192.168.0.1"));
            
            int i = 16 & 0xF0;
            System.out.println(i);
            
            i = 17  & 0xF0;
            System.out.println(i);
            
        }
        catch(Exception e)
        {
            
        }
        
    }
}
