
/**
* File: SecureUtil.java
*
* Purpose: 
*
* @author xuji
* @version 1.0 2007-6-16
*
* Copyright (C) 2004, 2008, HC, Inc.
*
*/

package com.jutongji.util;


import com.jutongji.util.security.Base64;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Random;


/**
 * 加密相关函数。用到bouncycastle
 */
public class SecureUtil
{
    private static Logger logger = LoggerFactory.getLogger(SecureUtil.class.getName());

    private static final int IV_LEN = 8;

    private static final String ENC_FLAG = "~|!&";

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] enc3DES(byte[] inputData, byte[] key, boolean useIv) throws Exception
    {

        BlockCipher engine = new DESedeEngine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));

        cipher.init(true, new KeyParameter(key));

        byte[] input;
        if (useIv)
        {
            input = new byte[inputData.length + IV_LEN];
            new Random().nextBytes(input);
            System.arraycopy(inputData, 0, input, IV_LEN, inputData.length);
        }
        else
        {
            input = inputData;
        }

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int outputLen = cipher.processBytes(input, 0, input.length, cipherText, 0);
        try
        {
            cipher.doFinal(cipherText, outputLen);
        }
        catch (CryptoException e)
        {
            String err = "加密数据失败，异常：" + e.getMessage();
            logger.error(err, e);
            throw new Exception(err, e);
        }

        return cipherText;
    }

    public static byte[] dec3DES(byte[] input, byte[] key, boolean useIv) throws Exception
    {

        BlockCipher engine = new DESedeEngine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));

        cipher.init(false, new KeyParameter(key));

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

        int outputLen = cipher.processBytes(input, 0, input.length, cipherText, 0);
        try
        {
            outputLen += cipher.doFinal(cipherText, outputLen);
        }
        catch (CryptoException e)
        {
            String err = "解密数据失败，异常：" + e.getMessage();
            logger.error(err, e);
            throw new Exception(err, e);
        }

        if (useIv)
        {
            // 切除头部随机数
            byte[] ret = new byte[outputLen - IV_LEN];
            System.arraycopy(cipherText, IV_LEN, ret, 0, ret.length);
            return ret;
        }
        else
            return cipherText;
    }

    public static String enc3DES(String input, byte[] key) throws Exception
    {
        // 增加加密标记，防止解密时误判
        byte[] encData = enc3DES((ENC_FLAG + input).getBytes(), key, true);
        return new String(Base64.encode(encData));
    }

    public static String tryDec3DES(String input, byte[] key) throws Exception
    {
        byte[] decData;
        
        try
        {
            decData = dec3DES(Base64.decode(input), key, true);
        }
        catch (Exception e)
        {
            // 不是加密数据
            return input;
        }

        String txt = new String(decData);
        if (txt.startsWith(ENC_FLAG))
        {
            return txt.substring(ENC_FLAG.length(), txt.length());
        }
        else
            return input;
    }

    /*
     * 用根证书验证证书
     */
    public static void verifyCert(String strCert, String strRootCert) throws Exception
    {
        Certificate cert = buildX509Cert(strCert);
        Certificate certRoot = buildX509Cert(strRootCert);

        cert.verify(certRoot.getPublicKey());
    }

    public static X509Certificate buildX509Cert(String cert) throws Exception
    {
        return (X509Certificate) CertificateFactory.getInstance("X509", new BouncyCastleProvider())
                .generateCertificate(new ByteArrayInputStream(Base64.decode(cert)));
    }

    public static byte[] envelopData(byte[] data, PublicKey pubKey) throws Exception
    {
        SecureRandom rand = new FixedSecureRandom();
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();

        // 公钥加密session key
        byte[] sessionKey = key.getEncoded();

        // Cipher c = Cipher.getInstance("RSA");
        // 最新的KPSDK(2006-5-19)使用PKCS1 1.5 Padding（老版本是NoPadding），如果
        // KPSDK这部分有变化，这里也需要调整
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        c.init(Cipher.ENCRYPT_MODE, pubKey, rand);
        byte[] encSessionKey = c.doFinal(sessionKey);

        // session key加密数据
        Cipher ecipher = Cipher.getInstance("DES");

        ecipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encData = ecipher.doFinal(data);

        StringBuffer ret = new StringBuffer(encSessionKey.length + encData.length + 20);

        ret.append("<sessionkey>");
        ret.append(new String(Base64.encode(encSessionKey)));
        ret.append("</sessionkey>");

        ret.append("<data>");
        ret.append(new String(Base64.encode(encData)));
        ret.append("</data>");

        return ret.toString().getBytes();
    }

    public static String encPbeWithSHAAndDES(String password, String plainText) throws Exception
    {
        String result = null;
        byte[] salt   = new byte[8];
        char[] passwd = new char[password.length()];
        
        Random random = new Random();
        random.nextBytes(salt);
        
        password.getChars(0, password.length() - 1, passwd, 0);
        
        PBEKeySpec keySpec = new PBEKeySpec(passwd);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 1000);
        
        Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        
        byte[] cipherText = cipher.doFinal(plainText.getBytes("ISO-8859-1"));
        
        String strSalt = new String(Base64.encode(salt));
        String strCipher = new String(Base64.encode(cipherText));
        
        result = strSalt + strCipher;
        
        return result;
        
    }
    
    public static String decPbeWithSHAAndDES(String password, String text) throws Exception
    {
        String result      = null;
        String salt        = text.substring(0, 12);
        String cipherText  = text.substring(12, text.length());
        char[] passwd      = new char[password.length()];
        
        
        password.getChars(0, password.length() - 1, passwd, 0);
        byte[] arySalt     = Base64.decode(salt);
        byte[] aryCipher   = Base64.decode(cipherText);
        
        PBEKeySpec keySpec = new PBEKeySpec(passwd);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDES");
        
        SecretKey key = keyFactory.generateSecret(keySpec);
        
        PBEParameterSpec paramSpec = new PBEParameterSpec(arySalt, 1000);
        
        Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDES");
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        
        byte[] plainText = cipher.doFinal(aryCipher);
        
        result = new String(plainText, "ISO-8859-1");
        
        return result;
        
    }
    
    private static class FixedSecureRandom extends SecureRandom
    {
        private static final long serialVersionUID = 1L;
        
        byte[] seed = {(byte) 0xaa, (byte) 0xfd, (byte) 0x12, (byte) 0xf6, (byte) 0x59, (byte) 0xca, (byte) 0xe6,
                (byte) 0x34, (byte) 0x89, (byte) 0xb4, (byte) 0x79, (byte) 0xe5, (byte) 0x07, (byte) 0x6d, (byte) 0xde,
                (byte) 0xc2, (byte) 0xf0, (byte) 0x6c, (byte) 0xb5, (byte) 0x8f};

        public void nextBytes(byte[] bytes)
        {
            int offset = 0;

            while ((offset + seed.length) < bytes.length)
            {
                System.arraycopy(seed, 0, bytes, offset, seed.length);
                offset += seed.length;
            }

            System.arraycopy(seed, 0, bytes, offset, bytes.length - offset);
        }
    }
    
    public static String encDES(String key, String plainText) throws Exception
    {
        String strEncrypt = plainText;
        
        if((null != plainText) && (!"".equals(plainText)))
        {
            try
            {
                SecretKeySpec deskey = new SecretKeySpec(key.getBytes(), "DES");
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.ENCRYPT_MODE, deskey);
                byte[] cipherByte = c.doFinal(plainText.getBytes());
                strEncrypt = new String(Base64.encode(cipherByte));
            }
            catch(Exception ex)
            {
                throw new Exception("加密失败");
            }
        }
        
        return strEncrypt;
    }
    
    public static String decDES(String key, String plainText) throws Exception
    {
        String strEncrypt = plainText;
        
        if((null != plainText) && (!"".equals(plainText)))
        {
            try
            {
                SecretKeySpec deskey = new SecretKeySpec(key.getBytes(), "DES");
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.DECRYPT_MODE, deskey);
                byte[] cipherByte = c.doFinal(Base64.decode(plainText.getBytes()));
                strEncrypt = new String(cipherByte);
            }
            catch(Exception ex)
            {
                throw new Exception("解密失败");
            }
        }
        
        return strEncrypt;
    }

    public static String byteArray2Hex(byte[] data)
    {
        BigInteger e = new BigInteger(1, data);
        return e.toString(16).toLowerCase();
    }
    
    
    public static String hashWithMD5(String text) throws Exception
    {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(text.getBytes("UTF-8"));
        return byteArray2Hex(md5.digest());
    }
    
    public static String hashWithMD5(byte[] bytes) throws Exception
    {
        int    bufferSize = 4096;
        byte[] buffer     = new byte[4096];
        
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        
        int remain = bytes.length;
        
        while(remain > 0)
        {
            int len = (remain > bufferSize) ? bufferSize : remain;
            System.arraycopy(bytes, bytes.length - remain, buffer, 0, len);
            remain = remain - len;
            
            md5.update(buffer, 0, len);
        }
        
        return byteArray2Hex(md5.digest());
    }
    
    public static String md5(String text) throws Exception
    {
        return md5(text.getBytes("UTF-8"));
    }
    
    public static String md5(byte[] source) throws Exception
    {
        int    bufferSize = 4096;
        byte[] buffer     = new byte[4096];
        
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        
        int remain = source.length;
        
        while(remain > 0)
        {
            int len = (remain > bufferSize) ? bufferSize : remain;
            System.arraycopy(source, source.length - remain, buffer, 0, len);
            remain = remain - len;
            
            md5.update(buffer, 0, len);
        }
        
        return byte2Hex(md5.digest());
    }
    
    public static String byte2Hex(byte[] bytes) throws Exception
    {
        final String HEX = "0123456789abcdef";
        
        String result = "";
        for(int i=0; i< bytes.length; i++)
        {
            result += HEX.charAt(bytes[i] >> 4 & 0x0F);
            result += HEX.charAt(bytes[i] & 0x0F);
        }
        
        return new String(result);
    }
    
    public static byte[] hex2Byte(String text) throws Exception
    {
        final String HEX = "0123456789abcdef";
        
        String hexText = text;
        if (text.length() % 2 == 1)
        {
            hexText = "0" + text;
        }
        hexText = hexText.toLowerCase();
        
        int     len     = hexText.length() / 2;
        byte[]  result  = new byte[len];
        for (int i = 0; i < len; i++)
        {
            String s1 = hexText.substring(2 * i, 2 * i + 1);
            String s2 = hexText.substring(2 * i + 1, 2 * i + 2);
            
            int h = HEX.indexOf(s1);
            int l = HEX.indexOf(s2);
            
            if ((h == -1) || (l == -1))
            {
                throw new InvalidParameterException();
            }
            
            result[i] = (byte)((h << 4) | (l & 0x0F));
        }
        
        return result;
    }
    
    public static void main(String[] args)
    {
        try
        {
            String s = "fab129faa1";
            byte[] b = hex2Byte(s);
            
            String s1 = byte2Hex(b);
            System.out.println(s);
            System.out.println(s1);
            
            if (s.equals(s1))
            {
                System.out.println("1111111111111111111111111111111");
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}





/**
 * $Log: SecureUtil.java,v $
 * Revision 1.1  2010/05/17 03:51:25  xuji
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/18 03:13:12  xuji
 * *** empty log message ***
 *
 * @ Revision history
 * @ -------------------------------------------------------------------------
 * @ Version      Date              Author               Note
 * @ -------------------------------------------------------------------------
 * @ 1.0          2007-6-16          xuji             create
 * @
 * @
 */
