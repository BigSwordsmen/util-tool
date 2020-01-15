/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 *
 * @author zhaoj
 * @version EncryptUtil.java, v 0.1 2019-03-13 11:39
 */
public class EncryptUtil {
    private static Logger LOG = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * account name 在TID 中的索引起始位置
     */
    private static final Integer TID_ACCOUNT_INDEX = 40;

    /**
     *  私钥
     */
    public static String  A_KEY = "www.kesion.comusername";


    /**
     * 根据 TID 获取 AccountName
     * @param tid
     * @return
     */
    public static String getAccountNameByTid(String tid) {
        if (tid != null && tid.length() > TID_ACCOUNT_INDEX) {
            int endFlag = tid.lastIndexOf("@");
            if(endFlag > TID_ACCOUNT_INDEX) {
                return tid.substring(TID_ACCOUNT_INDEX, endFlag);
            }
            return tid.substring(TID_ACCOUNT_INDEX);
        }
        return null;
    }


    /**
     * 明文加密(主要用于对 API code 进行加密)
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(A_KEY.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes("utf-8");
            byte[] result = cipher.doFinal(byteContent);
            Base64 base64 = new Base64();
            return new String(base64.encode(result));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 密文解密(主要用于对 API code 密文进行解密)
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(A_KEY.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            Base64 base64 = new Base64();
            byte[] byteContent = base64.decode(content.getBytes());
            byte[] result = cipher.doFinal(byteContent);
            return new String(result);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String content = "123456";
        String ciphertext = EncryptUtil.encrypt(content);
        String cleartext = EncryptUtil.decrypt(ciphertext);
        LOG.info("content:{} ,encrypt:{}, decrypt:{} ", content, ciphertext, cleartext);
        Assert.assertEquals(content, cleartext);
    }
}
