/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.digest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *签名摘要算法工具类
 * @author zhaoj
 * @version DigestUtil.java, v 0.1 2019-03-13 10:26
 */
public class DigestUtil {
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 生成摘要
     * @param content    文本
     * @param algorithm  摘要算法
     * @return 返回16进制摘要信息
     */
    public static String genDigest(String content, String algorithm) {
        try {
            byte[] bytes = content.getBytes(CHARSET_NAME);
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes);
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成md5
     * @param content
     * @return
     */
    public static String genMD5(String content) {
        return genDigest(content, "MD5");
    }

    /**
     * 生成SHA-1摘要
     * @param content
     * @return
     */
    public static String genSHA1(String content) {
        return genDigest(content, "SHA-1");
    }

    public static void main(String[] args) {
        String content = "123456";
        System.out.println(genMD5(content));
        System.out.println(genMD5(content).length());
        System.out.println(genSHA1(content));
        System.out.println(genSHA1(content).length());
    }
}
