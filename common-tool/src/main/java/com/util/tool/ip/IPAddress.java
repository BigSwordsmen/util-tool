/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.ip;


import com.util.tool.exception.InvalidIPAddressException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip表示,支持ipv4,ipv6
 * @author zhaoj
 * @version IPAddress.java, v 0.1 2019-03-13 10:31
 */
public class IPAddress {
    public static final int IPv4 = 32;
    public static final int IPv6 = 128;

    private static final int IPv4BlockNum = 8;
    private static final int IPv6BlockNum = 16;

    private static final String IPv4Separtor = ".";
    private static final String IPv6Separtor = ":";
    private static final char padChar = '0';

    private static final Pattern patternBinaryV4 = Pattern.compile("^[0-1]{32}$");
    private static final Pattern patternBinaryV6 = Pattern.compile("^[0-1]{128}$");
    private static final Pattern patternV6Blank = Pattern.compile("(:0){2,8}");

    /**
     * ipv4 =1,ipv6=2
     */
    private int ipVersion = 0;

    /**
     * 存放ip的二进制信息
     */
    private String ipBinaryStr = "";

    public IPAddress(String ipAddressStr) {
        parseIPAddress(ipAddressStr);
    }

    private void parseIPAddress(String ipAddressStr) {
        // 不能为空
        if (StringUtils.isBlank(ipAddressStr)) {
            throw new InvalidIPAddressException("IP is blank!");
        }
        if (ipAddressStr.indexOf(IPv4Separtor) != -1) {
            // ipv4的标准解析:192.168.1.1
            ipVersion = IPv4;
            if (!IpUtil.isIpCorrect(ipAddressStr)) {
                throw new InvalidIPAddressException("IP:" + ipAddressStr + " 不符合IPv4格式！");
            }
            String[] temps = ipAddressStr.split("\\.");
            for (String temp : temps) {
                ipBinaryStr += StringUtils.leftPad(Integer.toBinaryString(Integer.parseInt(temp)), IPv4BlockNum, padChar);
            }
        } else if (ipAddressStr.indexOf(IPv6Separtor) != -1) {
            // ipv6的标准解析:2001:0DB8:02de:0000:0000:0000:0000:0e13
            ipVersion = IPv6;
            if (!IpUtil.isIpv6Correct(ipAddressStr)) {
                throw new InvalidIPAddressException("IP:" + ipAddressStr + " 不符合IPv6格式！");
            }
            if (ipAddressStr.indexOf("::") != -1) {
                ipAddressStr = formatIpV6(ipAddressStr);
            }
            String[] temps = ipAddressStr.split(IPv6Separtor);
            for (String temp : temps) {
                ipBinaryStr += StringUtils.leftPad(Integer.toBinaryString(Integer.valueOf(temp, 16)), IPv6BlockNum, padChar);
            }
        } else if (ipAddressStr.length() == IPv4) {
            ipVersion = IPv4;
            Matcher matcher = patternBinaryV4.matcher(ipAddressStr);
            if (!matcher.matches()) {
                throw new InvalidIPAddressException("IP:" + ipAddressStr + " 不符合IPv4格式！");
            }
            ipBinaryStr = ipAddressStr;
        } else if (ipAddressStr.length() == IPv6) {
            ipVersion = IPv6;
            Matcher matcher = patternBinaryV6.matcher(ipAddressStr);
            if (!matcher.matches()) {
                throw new InvalidIPAddressException("IP:" + ipAddressStr + " 不符合IPv6格式！");
            }
            ipBinaryStr = ipAddressStr;
        } else {
            throw new InvalidIPAddressException("IP:" + ipAddressStr + "  格式不规范！");
        }

    }

    private String formatIpV6(String ipv6Str) {
        int colonCount = StringUtils.countMatches(ipv6Str, IPv6Separtor);
        int count = 8 - colonCount;
        String result = "";
        for (int i = 0; i < count; i++) {
            result += ":0000";
        }
        result += IPv6Separtor;
        result = ipv6Str.replaceAll("::", result);
        if (result.endsWith(IPv6Separtor)) {
            result += "0000";
        }
        if (result.startsWith(IPv6Separtor)) {
            result = "0000" + result;
        }
        return result;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * @param simple
     *            简写ipv6
     * @return
     */
    public String toString(boolean simple) {
        StringBuilder result = new StringBuilder();
        if (ipVersion == IPv4) {
            int temp;
            int count = IPv4 / IPv4BlockNum;
            for (int i = 0; i < count; i++) {
                temp = Integer.valueOf(StringUtils.substring(ipBinaryStr, i * IPv4BlockNum, (i + 1) * IPv4BlockNum), 2);
                result.append(temp);
                if (i < (count - 1)) {
                    result.append(IPv4Separtor);
                }
            }
        } else if (ipVersion == IPv6) {
            int temp;
            int count = IPv6 / IPv6BlockNum;
            for (int i = 0; i < count; i++) {
                temp = Integer.valueOf(StringUtils.substring(ipBinaryStr, i * IPv6BlockNum, (i + 1) * IPv6BlockNum), 2);
                String block = Integer.toHexString(temp);
                result.append(simple ? block : StringUtils.leftPad(block, 4, '0'));
                if (i < (count - 1)) {
                    result.append(IPv6Separtor);
                }
            }
            if (simple) {
                String resultString = result.toString();
                Matcher matcher = patternV6Blank.matcher(resultString);
                // 只匹配到一次
                if (matcher.find() && !matcher.find()) {
                    resultString = resultString.replaceAll(patternV6Blank.toString(), IPv6Separtor);
                }
                if (resultString.endsWith(IPv6Separtor)) {
                    resultString += IPv6Separtor;
                }

                if (resultString.startsWith("0::")) {
                    resultString = resultString.substring(1);
                }
                return resultString;
            }
        }
        return result.toString();
    }

    public String toBinaryString() {
        return ipBinaryStr;
    }

    public int getIpVersion() {
        return ipVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ipBinaryStr == null) ? 0 : ipBinaryStr.hashCode());
        result = prime * result + ipVersion;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IPAddress other = (IPAddress) obj;
        if (ipBinaryStr == null) {
            if (other.ipBinaryStr != null) {
                return false;
            }
        } else if (!ipBinaryStr.equals(other.ipBinaryStr)) {
            return false;
        }
        if (ipVersion != other.ipVersion) {
            return false;
        }
        return true;
    }

    public static boolean validIp(String ipString) {
        try {
            new IPAddress(ipString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String ip = "172.16.20.112";
        System.out.println(validIp(ip));
        ip = "2000:0000:0000:0000:0001:2345:6789:abcd";
        System.out.println(validIp(ip));
    }
}
