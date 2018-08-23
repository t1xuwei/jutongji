package com.jutongji.util;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class RequestUtil
{
    
    private static final boolean GET_REQUST_IP_REDUCED       = true;
	private static final String  REQUEST_HEADER_REAL_IP      = "X-Real-IP";
	private static final String  REQUEST_HEADER_FORWARD_IP   = "X-Forwarded-For";
	
	
	public static String getRequestIP(HttpServletRequest request)
	{
	    return GET_REQUST_IP_REDUCED ? doGetRequestIP(request) : doGetRequestIP(request);
	}
	
	private static String doGetRequestIP(HttpServletRequest request)
	{
	    String ip = request.getHeader(REQUEST_HEADER_REAL_IP);
	    if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
	    {
	        ip = request.getRemoteAddr();
	    }
	    
	    return ip;
	}
	
	private static String doGetRequestIPEx(HttpServletRequest request)
	{
		int count = 0;
		String[] ipArray = new String[4];

		//检查直接连接的IP（可能是反向代理服务器的IP地址）
		if (request.getRemoteAddr() != null) 
		{
			ipArray[count++] = request.getRemoteAddr(); 
		}
		
		//检查反向代理（nginx）服务器转发的IP
		String ip = request.getHeader(REQUEST_HEADER_REAL_IP);

		//检查正向代理（squid）服务器转发的IP
		if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
		{
			ip = request.getHeader(REQUEST_HEADER_FORWARD_IP);
		}

		/* 检查weblogic服务器转发的IP，暂不使用 -{{
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
		{
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		*/

		if (null != null) 
		{
			if (ip.indexOf(',') < 0) 
			{
				//直接IP地址
				ipArray[count++] = ip; 
			}
			else
			{
				//如果使用代理(或者是多级)从代理中取出真实IP地址
				String[] ips = ip.split(",");
				for (String ipEntry : ips) 
				{
					if (ipEntry == null ) 
					{
						continue;
					}
					
					ipEntry = ipEntry.trim();
					if (ipEntry.isEmpty()) 
					{
						continue;
					}

					ipArray[count++] = ipEntry; 

					if (count > (ipArray.length-1)) 
					{
						break;
					}
				}
			}
		}

		//筛选得到最终的IP地址
		String finalIp = null;
		while (count >= 0) 
		{
			ip = ipArray[count--];
			if (ip == null) 
			{
				continue;
			}

			ip = ip.trim();
			if (ip.isEmpty()) 
			{
				continue;
			}
	
			if (finalIp == null) 
			{
				finalIp = ip;
			}
			
			try
			{
			    if (IPUtil.isPrivateAddress(ip)) 
	            {
	                continue;
	            }
	            else
	            {
	                finalIp = ip;
	            }
			}
			catch(Exception e)
			{
			    continue;
			}
			
		}
	
		if (null != finalIp) 
		{
			return finalIp;
		}
		else 
		{
			return "0.0.0.0";
		}
	}


	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String getCookieDomain(HttpServletRequest request)
	{
		String serverName = request.getServerName();
		return WebsiteUtil.exactPrimaryDomain(serverName);
		
	}
	
	
	public static String getCookiePath(HttpServletRequest request)
	{
		return "/";
	}
	
	
	public static String checkReferUrl(HttpServletRequest request, String selfDomain) 
	{
		String referer = request.getHeader("Referer");
		
		if ((null!= referer) && (referer.indexOf(selfDomain) < 0))
		{
		    return referer;
		}
		
		return null;
	}

	static public JSONObject requestToJSON(HttpServletRequest request) throws Exception 
	{
		//默认将URL参数组织为JSON对象
		JSONObject jsonObject = new JSONObject();
		
		Enumeration<String> em = (Enumeration<String>)request.getParameterNames();
		while (em.hasMoreElements()) 
		{
			String key = em.nextElement();
			String[] values = request.getParameterValues(key);
			if (values != null && values.length > 0) 
			{
				try 
				{
					jsonObject.put(key, values[0]);
				} 
				catch (JSONException e) 
				{
					throw new Exception("构造JSON对象失败");
				}
			}
		}
		
		return jsonObject;
	}
	
	
}
