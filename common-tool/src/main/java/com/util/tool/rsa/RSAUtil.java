/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.rsa;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.util.tool.log.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaoj
 * @version RSAUtil.java, v 0.1 2019-04-12 11:36
 */
@Slf4j
public class RSAUtil {
    /**
     * 生成公钥和私钥
     *
     * @return
     */
    public static Map<String, String> generateKeyBase64() {
        Map<String, String> base64Map = new ConcurrentHashMap<>();
        final RSA rsa = new RSA();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        base64Map.put("privateKeyBase64", privateKeyBase64);
        base64Map.put("publicKeyBase64", publicKeyBase64);
        return base64Map;
    }

    /**
     * 生产公私钥
     *
     * @return
     */
    public static RSA generateRSA() {
        return new RSA();
    }

    /**
     * 私钥生成签名
     *
     * @param privateKeyBase64
     * @param params
     * @return
     */
    public static String generateRSASign(String privateKeyBase64, Map<String, String> params) {
        return generateRSASign(privateKeyBase64, getSignContent(params));
    }

    /**
     * 私钥生成签名
     *
     * @param privateKeyBase64
     * @param params
     * @return
     */
    public static String generateRSASign(String privateKeyBase64, String params) {

        //使用私钥通过SHA1withRSA加密方式生成签名
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, privateKeyBase64, null);
        String encodeSign = null;
        try {
            //将签名进行base64编码
            encodeSign = Base64.encode(sign.sign(params.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(log, "私钥生成签名异常,param={},exception={}", params, e);
        }
        return encodeSign;
    }

    /**
     * 私钥生成签名
     *
     * @param privateKeyBase64
     * @param params
     * @return
     */
    public static String generateSHA256WithRSASign(String privateKeyBase64, String params) {
        String signStr = null;
        try {
            Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64)));
            sign.init(SignAlgorithm.SHA256withRSA.getValue(), privateKey, null);
            signStr = Base64.encode(sign.sign(params.getBytes("UTF-8")));
        } catch (Exception e) {
            LogUtil.error(log, "generateSHA256WithRSASign >> 私钥生成签名异常,param={}", e, params);
        }
        return signStr;
    }

    /**
     * 公钥验签
     *
     * @param publicKeyBase64
     * @param params
     * @param encodeSign
     */
    public static boolean verityRSAVeritySign(String publicKeyBase64, String params, String encodeSign) {
        //使用公钥钥通过SHA1withRSA加密方式生成签名
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, publicKeyBase64);
        //判断签名是否一致
        return sign.verify(params.getBytes(), Base64.decode(encodeSign.getBytes()));
    }

    /**
     * 公钥验签
     *
     * @param publicKeyBase64
     * @param params
     * @param encodeSign
     * @return
     */
    public static boolean verityRSAVeritySign(String publicKeyBase64, Map<String, String> params, String encodeSign) {
        //使用公钥钥通过SHA1withRSA加密方式生成签名
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, publicKeyBase64);
        //判断签名是否一致
        return sign.verify(getSignContent(params).getBytes(), Base64.decode(encodeSign.getBytes()));
    }

    /**
     * 拼接加密内容
     *
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.isNoneBlank(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    public static void main(String[] args) {
        //在线生成公私钥匙地址：http://web.chacuo.net/netrsakeypair
        String privateKeyBase64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKeKBnmZ5j9nk7N+5XDpwRFJrBFxJCE9/3G4GmdsfCOeQZs4oP3/jh62Ko1JHdfYH70Bx3aqz3mgdEp1DMfX2wHC98lJqQC1UQRUAWMgSk/w7ydeW2/N2gUZNmZne4pORLfiJIr9r0/ByqKxW4Ik2FQR3zevApYUbvi6PTr4YD+xAgMBAAECgYBFVLx+ON5L8cBxILuxIMKpNUwUAbaaXQ4nELE4iG/My5JJukMBKW62LdtpEU9cwY7Mx/4zkNb/9ZL6Nvr9fkmeAf+TxO/TEtm2H0Deg1c4ClnFJmbMmJrnYO5v/XCGN8v88/yHzDSE2wP1Vl9ibi8D1nzlt6o6RoBqTayECdVtUwJBAOfv3NTo7ALFZQtorA0hepKQI0aj1fEUjqPFS0Ot4IiKbJ2wSkz6GV+M/612+OFzaKAtBI8BSHIGftk6GaDvtGcCQQC468mBuBcgXWhKTOjueKTy5X5lkGPVGhPGzlp5b+LstKDHW/ZqG6Ix7CcnUjzMk61mM5LUfaOVEFQaEL1ii5wnAkAZnY4gHZFjRPXB6s+Fq7rj0PN0a8fHFmQihjmnwd5YdiFE0SGDmuOOf/4E5GzcSWi2uAIW4SdlIH6F8zq9YXvlAkEAh96vI0G4tE4NZ7JU34sDX3jhwwwxXg2YBFXwQhPEfzlEfNaEGniNVvL13b1d8M589H/mXXym6cqikLhF/pcntwJBAJXcKX8lZo4lGVrTcSGFm10KlOpfar2JVxwJqNRohdVa5VZC85t20c2eTF5gODFv/85EFpXnTtYAcm11vyHhnlA=";
        Map<String, String> params = new HashMap<>();
        params.put("version", "1.0");
        params.put("name", "zhaojun");
        String encodeSign = generateRSASign(privateKeyBase64, params);
        System.out.println(encodeSign);
        String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnigZ5meY/Z5OzfuVw6cERSawRcSQhPf9xuBpnbHwjnkGbOKD9/44etiqNSR3X2B+9Acd2qs95oHRKdQzH19sBwvfJSakAtVEEVAFjIEpP8O8nXltvzdoFGTZmZ3uKTkS34iSK/a9PwcqisVuCJNhUEd83rwKWFG74uj06+GA/sQIDAQAB";
        Boolean isVerity = verityRSAVeritySign(publicKeyBase64, params, encodeSign);
        System.out.println(isVerity);

        Map<String, String> stringMap = generateKeyBase64();
        System.out.println(stringMap.toString());

        RSA rsa = generateRSA();
        System.out.println("privateKeyBase64====" + rsa.getPrivateKeyBase64());
        System.out.println("publicKeyBase64====" + rsa.getPublicKeyBase64());


    }
}
