/**
 * @(#)WebsiteUtil.java
 *
 * @author xuji
 * @version 1.0 2012-8-3
 *
 * Copyright (C) 2000,2012 , KOAL, Inc.
 */
package com.jutongji.util;

import com.jutongji.util.str.StringUtils;
import com.jutongji.util.str.StringValidator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Purpose:
 *
 * @see
 * @since   1.1.0
 */
public class WebsiteUtil
{
    public static String[] TOP_DOMAIN = new String[]{
        ".gov.mo",
        ".com.tw",
        ".com.mo",
        ".co.cc",
        ".ce.ms",
        ".osa.pl",
        ".c.la",
        ".com.hk",
        ".net.in",
        ".edu.tw",
        ".org.tw",
        ".bij.pl",
        ".ac.cn",
        ".ah.cn",
        ".bj.cn",
        ".com.cn",
        ".cq.cn",
        ".fj.cn",
        ".gd.cn",
        ".gov.cn",
        ".gs.cn",
        ".gx.cn",
        ".gz.cn",
        ".ha.cn",
        ".hb.cn",
        ".he.cn",
        ".hi.cn",
        ".hk.cn",
        ".hl.cn",
        ".hn.cn",
        ".jl.cn",
        ".js.cn",
        ".jx.cn",
        ".ln.cn",
        ".mo.cn",
        ".net.cn",
        ".nm.cn",
        ".nx.cn",
        ".org.cn",
        ".qh.cn",
        ".sc.cn",
        ".sd.cn",
        ".sh.cn",
        ".sn.cn",
        ".sx.cn",
        ".tj.cn",
        ".tw.cn",
        ".xj.cn",
        ".xz.cn",
        ".yn.cn",
        ".zj.cn",
        ".nl.ae",
        ".org.uk",
        ".org.nz",
        ".org.bz",
        ".org.au",
        ".com.nu",
        ".com.my",
        ".com.au",
        ".co.uk",
        ".co.kr",
        ".co.jp",
        ".nu.ae",
        ".nl.ae",
        ".com.au",
        ".cf.gs",
        ".com.cn",
        ".net.cn",
        ".org.cn",
        ".edu.cn",
        ".com",
        ".cn",
        ".mobi",
        ".tel",
        ".asia",
        ".net",
        ".org",
        ".name",
        ".me",
        ".info",
        ".cc",
        ".hk",
        ".biz",
        ".tv",
        ".la",
        ".fm",
        ".cm",
        ".am",
        ".sh",
        ".us",
        ".in",
        ".ro",
        ".ru",
        ".hu",
        ".tk",
        ".co",
        ".cx",
        ".at",
        ".tw",
        ".ws",
        ".vg",
        ".vc",
        ".uz",
        ".to",
        ".tl",
        ".th",
        ".tf",
        ".tc",
        ".st",
        ".so",
        ".sk",
        ".sg",
        ".sc",
        ".pl",
        ".pe",
        ".nu",
        ".nf",
        ".ne",
        ".my",
        ".mu",
        ".ms",
        ".mo",
        ".lv",
        ".lt",
        ".lc",
        ".jp",
        ".it",
        ".io",
        ".im",
        ".ie",
        ".gs",
        ".gp",
        ".gl",
        ".gg",
        ".gd",
        ".fr",
        ".fi",
        ".eu",
        ".edu",
        ".dk",
        ".de",
        ".cz",
        ".ch",
        ".ca",
        ".bi",
        ".be",
        ".au",
        ".ae",
        ".pw",
        ".ly",
        ".wang",
        ".ren",
        ".top",
        ".club"
    };

    /**
     *
     * exactDomain：<提取url中domain信息>
     *
     * @param url 例如：http://www.baidu.com/, http://a.baidu.com, http://www.abc.com:8080, http://www.xyz.com/abc/123.html
     * @return www.baidu.com, a.baidu.com, www.abc.com, www.xyz.com
     *
     */
    public static String exactDomain(String url)
    {
        String domain = url.toLowerCase();
        domain = StringUtils.deleteWhitespace(url);
        domain = domain.replace("https://", "");
        domain = domain.replace("http://", "");

        int pos = domain.indexOf("/");
        if (pos > 0)
        {
            domain = domain.substring(0, pos);
        }

        pos = domain.indexOf(":");
        if (pos > 0)
        {
            domain = domain.substring(0, pos);
        }

        return domain;
    }

    /**
     *
     * exactDomain：<提取url中domain信息>
     *
     * @param url 例如：http://www.baidu.com/, http://a.baidu.com, http://www.abc.com:8080, http://www.xyz.com/abc/123.html
     * @return www.baidu.com, a.baidu.com, www.abc.com, www.xyz.com
     *
     * @see <参见的内容>
     */
    public static String exactDomain(String url, boolean remove3W)
    {
        String domain = exactDomain(url);

        if (remove3W && domain.indexOf("www.") == 0)
        {
            domain = domain.substring(4);
        }

        return domain;
    }

    /**
     *
     * exactPrimaryDomain：<提取url中的主domain>
     *
     * @param url 例如 http://abc.xyz.com, http://www.xyz.com:8080/index.html, http://a.b.c.xyz.com/
     * @return xyz.com
     *
     * @see <参见的内容>
     */
    public static String exactPrimaryDomain(String url)
    {
        String domain = exactDomain(url);

        for (String str : TOP_DOMAIN)
        {
            if (domain.endsWith(str))
            {
                String   sub      = domain.substring(0, domain.length() - str.length());
                String[] children = sub.split("[.]");

                if (null != children && children.length > 0)
                {
                    return children[children.length - 1] + str;
                }
            }
        }

        return domain;
    }

    /**
     *
     * resolveAddress：<解析ip地址>
     *
     * @param domain
     * @return null when unknown host
     *
     * @see <参见的内容>
     */
    public static String resolveAddress(String domain) throws Exception
    {
        try
        {
            InetAddress ia = InetAddress.getByName(domain);
            return ia.getHostAddress().toString();
        }
        catch(UnknownHostException uhe)
        {
            return null;
        }
    }




    /**
     * URL验证
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url, boolean notAllowNull)
    {
        if (notAllowNull && (url == null || "".equals(url)))
        {
            return false;
        }

        if (url.split("[.]").length < 2)
        {
            return false;
        }

        if (url != null)
        {
            for (int i = 0; i < url.length(); i++)
            {
                String c = String.valueOf(url.charAt(i));

                if (i == url.length() - 1 && c.equals("#"))
                {
                    return true;
                }

                if (StringValidator.isLetter(c) // 英文
                        || StringValidator.isNumber(c) // 数字
                        || ":".equals(c) || ".".equals(c) || "-".equals(c) || "_".equals(c) || "/".equals(c) || "?".equals(c) || "&".equals(c) || "=".equals(c))
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean validateDomain(String domain)
    {
        String reg = "^([\\w-|\u4E00-\u9FA5]+\\.)+((com)|(net)|(org)|(gov\\.cn)|(info)|(cc)|(com\\.cn)|(net\\.cn)|(org\\.cn)|(name)|(biz)|(tv)|(cn)|(mobi)|(name)|(sh)|(ac)|(io)|(tw)|(com\\.tw)|(hk)|(com\\.hk)|(ws)|(travel)|(us)|(tm)|(la)|(me\\.uk)|(org\\.uk)|(ltd\\.uk)|(plc\\.uk)|(in)|(eu)|(it)|(jp)|(co)|(me)|(mx)|(ca)|(ag)|(com\\.co)|(net\\.co)|(nom\\.co)|(com\\.ag)|(net\\.ag)|(fr)|(org\\.ag)|(am)|(asia)|(at)|(be)|(bz)|(com\\.bz)|(net\\.bz)|(net\\.br)|(com\\.br)|(de)|(es)|(com\\.es)|(nom\\.es)|(org\\.es)|(fm)|(gs)|(co\\.in)|(firm\\.in)|(gen\\.in)|(ind\\.in)|(net\\.in)|(org\\.in)|(jobs)|(ms)|(com\\.mx)|(nl)|(nu)|(co\\.nz)|(net\\.nz)|(org\\.nz)|(tc)|(tk)|(org\\.tw)|(idv\\.tw)|(co\\.uk)|(vg)|(ad)|(ae)|(af)|(ai)|(al)|(an)|(ao)|(aq)|(ar)|(as)|(au)|(aw)|(az)|(ba)|(bb)|(bd)|(bf)|(bg)|(bh)|(bi)|(bj)|(bm)|(bn)|(bo)|(br)|(bs)|(bt)|(bv)|(bw)|(by)|(cd)|(cf)|(cg)|(ch)|(ci)|(ck)|(cl)|(cm)|(cr)|(cu)|(cv)|(cx)|(cy)|(cz)|(dj)|(dk)|(dm)|(do)|(dz)|(ec)|(ee)|(eg)|(er)|(et)|(fi)|(fj)|(fk)|(fo)|(ga)|(gd)|(ge)|(gf)|(gg)|(gh)|(gi)|(gl)|(gm)|(gn)|(gp)|(gq)|(gr)|(gt)|(gu)|(gw)|(gy)|(hm)|(hn)|(hr)|(ht)|(hu)|(id)|(ie)|(il)|(im)|(iq)|(ir)|(is)|(je)|(jm)|(jo)|(ke)|(kg)|(kh)|(ki)|(km)|(kn)|(kr)|(kw)|(ky)|(kz)|(lb)|(lc)|(li)|(lk)|(lr)|(ls)|(lt)|(lu)|(lv)|(ly)|(ma)|(mc)|(md)|(mg)|(mh)|(mk)|(ml)|(mm)|(mn)|(mo)|(mp)|(mq)|(mr)|(mt)|(mu)|(mv)|(mw)|(my)|(mz)|(na)|(nc)|(ne)|(nf)|(ng)|(ni)|(no)|(np)|(nr)|(nz)|(om)|(pa)|(pe)|(pf)|(pg)|(ph)|(pk)|(pl)|(pm)|(pn)|(pr)|(ps)|(pt)|(pw)|(py)|(qa)|(re)|(ro)|(ru)|(rw)|(sa)|(sb)|(sc)|(sd)|(se)|(sg)|(si)|(sk)|(sl)|(sm)|(sn)|(so)|(sr)|(st)|(sv)|(sy)|(sz)|(td)|(tf)|(tg)|(th)|(tj)|(tl)|(tn)|(to)|(tr)|(tt)|(tz)|(ua)|(ug)|(uk)|(uy)|(uz)|(va)|(vc)|(ve)|(vi)|(vn)|(vu)|(wf)|(ye)|(yt)|(yu)|(za)|(zm)|(zw))$";
        Pattern pattern = Pattern.compile(reg);
        Matcher mat = pattern.matcher(domain);

        return mat.matches();
    }

    public static boolean isPrimaryDomain(String domain)
    {
        if (!validateDomain(domain))
        {
            return false;
        }

        String pd = exactPrimaryDomain(domain);
        return domain.equalsIgnoreCase(pd);
    }

    public static boolean isDomainValid(String domain)
    {
        if (null == domain || "".equals(domain.trim()))
        {
            return false;
        }
        String url = domain;
        if(!domain.startsWith("http://") && !domain.startsWith("https://"))
        {
            if(isPrimaryDomain(domain))
            {
                url = "http://www."+domain;
            }
            else
            {
                url = "http://"+domain;
            }
        }

        GetMethod method = null;
        try
        {
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setParameter(httpClient.getParams().USER_AGENT, "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; 360SE)");
            method = new GetMethod(url);
            setRequestHeaders(method);

            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(300000);
            httpClient.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 300000);

            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                return false;
            }

            return true;
        }
        catch (HttpException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (null != method)
            {
                method.releaseConnection();
            }
        }
    }

    public static boolean isConnect(String domain)
    {
        int trytimes = 3;
        int counts = 0;
        if(domain == null || "".equals(domain.trim()))
        {
            return false;
        }
        domain = exactDomain(domain, true);
        String urlStr = domain;
        if(isPrimaryDomain(domain))
        {
            urlStr = "www."+domain;
        }
        while (counts < trytimes) {
            try
            {
                InetAddress ia = InetAddress.getByName(urlStr);
                if(ia!=null)
                {
                    return true;
                }
            }
            catch(UnknownHostException uhe)
            {
                counts++;
                continue;
            }
        }

        return false;
    }

    private static void setRequestHeaders(HttpMethod method)
    {
        if (null != method)
        {
            method.addRequestHeader("Connection", "keep-alive");
            method.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
            method.addRequestHeader("Pragma", "no-cache");
            //method.addRequestHeader("Accept-Encoding","gzip, deflate");
            //method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
    }

    public static void main(String[] args) throws Exception
    {
        try
        {

           /* String url = "http://werw.sdfs.www.zx110.org/";
            System.out.println(WebsiteUtil.exactDomain(url));
            System.out.println(WebsiteUtil.exactPrimaryDomain(url));

            url = "http://www.zx110.org";
            System.out.println(WebsiteUtil.exactDomain(url));
            System.out.println(WebsiteUtil.exactPrimaryDomain(url));

            url = "http://abc.zx110.org/";
            System.out.println(WebsiteUtil.exactDomain(url));
            System.out.println(WebsiteUtil.exactPrimaryDomain(url));

            url = "http://abc.zx110.org/abc/index.html";
            System.out.println(WebsiteUtil.exactDomain(url));
            System.out.println(WebsiteUtil.exactPrimaryDomain(url));

            String domain ="zx110.org";
            System.out.println(WebsiteUtil.resolveAddress(domain));

            domain ="ewtetretrewtrewtretre.com";
            System.out.println(WebsiteUtil.resolveAddress(domain));*/


  /*          System.out.println(WebsiteUtil.exactPrimaryDomain("192.168.78.89"));
            System.out.println(WebsiteUtil.resolveAddress("werwer21342342.com"));
            long t1 = System.currentTimeMillis();
            //System.out.println(WebsiteUtil.isDomainValid("lyshcn.com"));
            long t2 = System.currentTimeMillis();
            System.out.println(t2-t1);
            System.out.println(WebsiteUtil.isConnect("lyshcn.com"));
            long t3 = System.currentTimeMillis();
            System.out.println(t3-t2);*/
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}



/**
 * $Log: WebsiteUtil.java,v $
 *
 * @version 1.0 2012-8-3
 *
 */