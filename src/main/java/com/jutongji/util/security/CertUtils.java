/**
 * @(#)CertUtils.java
 *
 * @author xuji
 * @version 1.0 2014-7-9
 *
 * Copyright (C) 2012,2014 , PING' AN, Inc.
 */
package com.jutongji.util.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;

/**
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
 */
public class CertUtils
{
    public static X509Certificate buildX509Cert(String cert) throws Exception
    {
        String c = PEMFormat.certPEM2BER(cert);
        return (X509Certificate) CertificateFactory.getInstance("X509", new BouncyCastleProvider())
                .generateCertificate(new ByteArrayInputStream(Base64.decode(c)));
    }
    
    public static PrivateKey buildPrivateKey(String pem) throws Exception
    {
        String pkey = PEMFormat.privateKeyPEM2BER(pem);
        byte[] encoded = Base64.decode(pkey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        
        return kf.generatePrivate(keySpec);
    }
    
    public static X509Certificate buildX509CertFromFile(String path) throws Exception
    {
        FileInputStream fis = new FileInputStream(path);
        
        try
        {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate)cf.generateCertificate(fis);
        }
        finally
        {
            if (null != fis)
            {
                fis.close();
            }
        }
    }

    
    public static Collection buildX509CertsFromFile(String path) throws Exception
    {
        FileInputStream fis = new FileInputStream(path);
        
        try
        {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificates(fis);
        }
        finally
        {
            if (null != fis)
            {
                fis.close();
            }
        }
    }
    
    /*
     * 用根证书验证证书
     */
    public static void verifyCert(String b64Cert, String b64RootCert) throws Exception
    {
        Certificate cert = buildX509Cert(b64Cert);
        Certificate rootCert = buildX509Cert(b64RootCert);

        cert.verify(rootCert.getPublicKey());
    }

    public static String getCertItem(X509Certificate cert, String name)
    {
        String result = null;

        if (null == cert)
        {
            return null;
        }
        
        if (name.equals("SN"))
        {
            result = cert.getSerialNumber().toString();
        }
        else
        {
            String dn = cert.getSubjectDN().toString();
            result    = getItemFromDn(dn, name);
        }
        
        return result;
    }
    
    private static String getItemFromDn(String dn, String item)
    {
        String ret = "";
        String[] items = dn.split("[,]");
        for (int i = 0; i < items.length; i++)
        {
            String itemStr = items[i];
            if (itemStr.startsWith(item + "="))
            {
                ret = itemStr.substring((item + "=").length());
            }
        }
       
        return ret;
    }
    
    public static void main(String[] args)
    {
        try
        {
           X509Certificate cert = buildX509CertFromFile("c:\\cert\\test.cer");
           String dn = cert.getSubjectDN().toString();
           System.out.println(dn);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}


/**
 * $Log: CertUtils.java,v $
 * 
 * @version 1.0 2014-7-9 
 *
 */