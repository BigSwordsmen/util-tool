/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.ip;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhaoj
 * @version IpUtil.java, v 0.1 2019-03-13 10:40
 */
public class IpUtil {
    public static final Pattern patternIpV4 = Pattern
            .compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
    public static final Pattern patternIpV4Range = Pattern
            .compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])/(\\d|[1]\\d|[2]\\d|[3][0-2])$");
    public static final Pattern patternIpV6 = Pattern
            .compile("^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$");


    public static boolean isIpCorrect(String ipAddress) {
        Matcher matcher = patternIpV4.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isIpv6Correct(String ipAddress) {
        Matcher matcher = patternIpV6.matcher(ipAddress);
        return matcher.matches();
    }

    // 最小支持/24
    public static boolean isIpRangeCorrect(String ipRange) {
        Matcher matcher = patternIpV4Range.matcher(ipRange);
        return matcher.matches();
    }

    public static String getLocalHostname() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see "com.alibaba.staragent.channel.transport.util.RemotingUtil#getLocalAddress()"
     * @return
     */
    public static String getLocalAddress() {
        List<String> addressList = getLocalAddressList();
        if(addressList.isEmpty()) {
            try {
                return Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        } else {
            // 取最后一个
            return addressList.get(addressList.size() - 1);
        }
    }

    public static List<String> getLocalAddressList() {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            List<String> ipv4Result = new ArrayList<>();

            while(e.hasMoreElements()) {
                NetworkInterface localHost = (NetworkInterface)e.nextElement();
                Enumeration ip = localHost.getInetAddresses();
                while(ip.hasMoreElements()) {
                    InetAddress address = (InetAddress)ip.nextElement();
                    if(!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        String ip4 = address.getHostAddress();
                        if(ip4.startsWith("127.0") || ip4.startsWith("192.168")) {
                            // 排除此类IP
                        } else {
                            ipv4Result.add(ip4);
                        }
                    }
                }
            }
            // 如果上述过程都没有娶到ip
            if(ipv4Result.isEmpty()) {
                String s = Inet4Address.getLocalHost().getHostAddress();
                return Arrays.asList(s);
            }
            return ipv4Result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getLocalHostname());

        System.out.println(getLocalAddress());

        System.out.println(getLocalAddressList());

        assertTrue(IpUtil.isIpRangeCorrect("10.137.75.0/24"));

        assertTrue(IpUtil.isIpCorrect("10.137.75.5"));

        assertTrue(IpUtil.isIpv6Correct("686E:8C64:FFFF:FFFF:0:1180:96A:FFFF"));
    }
}
