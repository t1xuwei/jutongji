/*
import com.sun.jarsigner.ContentSigner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*
 *import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@(#)Signer.java
 *
 * @author xuji
 * @version 1.0 2014-7-9
 *
 * Copyright (C) 2012,2014 , PING' AN, Inc.
package com.jutongji.util.security;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


*
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
public class Signer
{
    private static final Logger logger = LoggerFactory.getLogger(Signer.class);
    
    private static String PROVIDER = "BC";
    private static String SIGN_ALG = "SHA1WithRSA";
    
    static 
    {
        if (Security.getProvider(PROVIDER) == null)
        {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
    
    protected PrivateKey key;
    protected X509Certificate cert;
    
    public Signer(PrivateKey key, X509Certificate cert)
    {
        this.key  = key;
        this.cert = cert;
    }
    
    public byte[] signPkcs7(byte[] originalMessage, boolean encapsulate) throws Exception
    {
        try
        {
            List certList = new ArrayList();
            certList.add(cert);
            Store certs = new JcaCertStore(certList);
            
            
            CMSSignedDataGenerator gen        = new CMSSignedDataGenerator();
            ContentSigner sha1Signer = new JcaContentSignerBuilder(SIGN_ALG).setProvider(PROVIDER).build(key);
            
            gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                    new JcaDigestCalculatorProviderBuilder().setProvider(PROVIDER).build()).build(sha1Signer, cert));
            gen.addCertificates(certs);
    
            CMSTypedData msg = new CMSProcessableByteArray(originalMessage);
            CMSSignedData sigData = gen.generate(msg, encapsulate);
            
            return sigData.getEncoded();
        }
        catch(Exception e)
        {
            logger.error("Signer::signPkcs7::pkcs7签名失败！{}", e.getMessage());
            throw new Exception("pkcs7签名失败！" + e.getMessage(), e);
        }
    }
    
    
}


*
 * $Log: Signer.java,v $
 * 
 * @version 1.0 2014-7-9 
 *
*/
