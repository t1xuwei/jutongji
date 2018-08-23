/**
 * @(#)CryptoUtils.java
 *
 * @author xuji
 * @version 1.0 2014-7-9
 *
 * Copyright (C) 2012,2014 , PING' AN, Inc.
 */
package com.jutongji.util.security;

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
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

/**
 * 
 * Purpose:
 * 
 * @see	    
 * @since   1.1.0
 */
public class CryptoUtils
{
    private static Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

    private static final int IV_LEN = 8;

    private static final String ENC_FLAG = "~|!&";

    
    private static String PROVIDER = "BC";
    
    static 
    {
        if (Security.getProvider(PROVIDER) == null) 
        {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static byte[] encDES(byte[] inputData, byte[] key) throws Exception
    {
        if((null != inputData) && (inputData.length > 0))
        {
            try
            {
                SecretKeySpec deskey = new SecretKeySpec(key, "DES");
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.ENCRYPT_MODE, deskey);
                return c.doFinal(inputData);
            }
            catch(Exception e)
            {
                throw new Exception("加密数据失败!" + e.getMessage());
            }
        }
        
        return null;
    }
    
    public static byte[] decDES(byte[] inputData, byte[] key) throws Exception
    {
        if((null != inputData) && (inputData.length > 0))
        {
            try
            {
                SecretKeySpec deskey = new SecretKeySpec(key, "DES");
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.DECRYPT_MODE, deskey);
                return c.doFinal(inputData);
            }
            catch(Exception e)
            {
                throw new Exception("解密数据失败!" + e.getMessage());
            }
        }
        
        return null;
    }
    
    public static String encDES(String input, byte[] key) throws Exception
    {
        //增加加密标记，防止解密时误判
        byte[] encData = encDES((ENC_FLAG + input).getBytes(), key);
        return new String(Base64.encode(encData));
    }

    public static String tryDecDES(String input, byte[] key) throws Exception
    {
        byte[] decData;
        
        try
        {
            decData = decDES(Base64.decode(input), key);
        }
        catch (Exception e)
        {
            //不是加密数据
            return input;
        }

        String txt = new String(decData);
        if (txt.startsWith(ENC_FLAG))
        {
            return txt.substring(ENC_FLAG.length(), txt.length());
        }
        else
        {
            return input;
        }
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
            //切除头部随机数
            byte[] ret = new byte[outputLen - IV_LEN];
            System.arraycopy(cipherText, IV_LEN, ret, 0, ret.length);
            return ret;
        }
        else
        {
            return cipherText;
        }
    }

    public static String enc3DES(String input, byte[] key) throws Exception
    {
        //增加加密标记，防止解密时误判
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
            //不是加密数据
            return input;
        }

        String txt = new String(decData);
        if (txt.startsWith(ENC_FLAG))
        {
            return txt.substring(ENC_FLAG.length(), txt.length());
        }
        else
        {
            return input;
        }
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

    public static String encPbeWithSHA1AndDES(String password, String plainText) throws Exception
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
    
    public static String decPbeWithSHA1AndDES(String password, String text) throws Exception
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

    
}


/**
 * $Log: CryptoUtils.java,v $
 * 
 * @version 1.0 2014-7-9 
 *
 */