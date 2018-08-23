/*
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*
 *import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Collection;

@(#)Verifier.java
 *
 * @author xuji
 * @version 1.0 2014-7-9
 *
 * Copyright (C) 2012,2014 , PING' AN, Inc.
package com.jutongji.util.security;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Collection;


*
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
public class Verifier
{
    private static final Logger logger = LoggerFactory.getLogger(Verifier.class);
    
    public static final int ERROR_VERIFIED           = 0;
    public static final int ERROR_NOT_VERIFIED       = 1;
    public static final int ERROR_INVALID_FORMAT     = 2;
    public static final int ERROR_CERT_NOT_VERIFIED  = 3;
    public static final int ERROR_CERT_NOT_FOUND     = 4;
    
    private static String PROVIDER = "BC";
    
    static 
    {
        if (null == Security.getProvider(PROVIDER))
        {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
    
    protected X509Certificate cert;
    
    public Verifier()
    {
        this(null);
    }
    
    public Verifier(X509Certificate cert)
    {
        this.cert = cert;
    }
    
    public X509Certificate getCert()
    {
        return cert;
    }

    public int verifyPkcs7(byte[] originalMessage, byte[] signature) throws Exception
    {
       try
       {
           CMSProcessableByteArray msg = new CMSProcessableByteArray(originalMessage);
           CMSSignedData signedData = new CMSSignedData(msg, signature);
           
           SignerInformationStore signers = signedData.getSignerInfos();
           SignerInformation signerInfo = (SignerInformation)signers.getSigners().iterator().next();
           
           //cert为null时则从p7包中获取
           if (null == cert)
           {
               Store store = signedData.getCertificates();
               Collection certs = store.getMatches(signerInfo.getSID());
               if (certs.size() == 0)
               {
                   return ERROR_CERT_NOT_FOUND;
               }
               
               X509CertificateHolder certHolder = (X509CertificateHolder)certs.iterator().next();
               X509Certificate x509cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
               
               if (verifyCert(x509cert))
               {
                   this.cert = x509cert;
               }
               else
               {
                   return ERROR_CERT_NOT_VERIFIED;
               }
           }
           
           boolean verified = signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert.getPublicKey()));
           
           return verified ? ERROR_VERIFIED : ERROR_NOT_VERIFIED;
       }
       catch(CMSSignerDigestMismatchException cme)
       {
           logger.error("Verifier::verifyPkcs7::签名验证不通过", cme);
           return ERROR_NOT_VERIFIED;
       }
       catch(CMSVerifierCertificateNotValidException cce)
       {
           logger.error("Verifier::verifyPkcs7::签名证书无效！", cce);
           return ERROR_CERT_NOT_VERIFIED;
       }
       catch(CMSException ce)
       {
           logger.error("Verifier::verifyPkcs7::无效的签名！", ce);
           return ERROR_INVALID_FORMAT;
       }
       catch(Exception e)
       {
           logger.error("Verifier::verifyPkcs7::验证签名发生未知异常！", e);
           throw new Exception("验证pkcs7签名失败!" + e.getMessage(), e);
       }
    }
    
    protected boolean verifyCert(X509Certificate cert)
    {
        return true;
    }
    
    
    public static void main(String[] args)
    {
        try
        {
           String keyFile = "c:\\cert\\test.key";
           String certFile = "c:\\cert\\test.cer";
           
           Signer signer = new Signer(keyFile, certFile);
           String originalText = "我爱ABC";
           byte[] data = signer.signPkcs7(originalText.getBytes("UTF-8"), false);
           String sig = new String(Base64.encode(data));
           System.out.println("签名：" + sig);
           
           String testText = "我爱ABC";
           Verifier verifier = new Verifier();
           int n = verifier.verifyPkcs7(testText.getBytes("UTF-8"), data);
           System.out.println(n);
           System.out.println(verifier.getCert().getSubjectDN().toString());
           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}


*
 * $Log: Verifier.java,v $
 * 
 * @version 1.0 2014-7-9 
 *
*/
