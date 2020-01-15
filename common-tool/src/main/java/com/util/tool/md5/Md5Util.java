/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.md5;

import com.util.tool.exception.CommonException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Locale;

/**
 *
 * @author zhaoj
 * @version Md5Util.java, v 0.1 2019-04-12 11:47
 */
public class Md5Util {
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @return 签名结果
     */
    public static String sign(String text) {
        return DigestUtils.md5Hex(getContentBytes(text, "UTF-8"));
    }

    /**
     * 签名字符串并将验签结果转化为大写
     * @param text 需要签名的字符串
     * @return 签名结果
     */
    public static String signToUpperCase(String text) {
        return DigestUtils.md5Hex(getContentBytes(text, "UTF-8")).toUpperCase(Locale.ENGLISH);
    }


    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String inputCharset) {
        return DigestUtils.md5Hex(getContentBytes(text, inputCharset));
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String inputCharset) {
        String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
        if(mysign.equals(sign)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new CommonException().newInstance("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:{0}",charset);
        }
    }
}
