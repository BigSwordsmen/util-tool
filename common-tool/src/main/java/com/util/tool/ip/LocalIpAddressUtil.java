/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 获取本地ip的工具
 * @author zhaoj
 * @version LocalIpAddressUtil.java, v 0.1 2019-03-13 10:53
 */
public class LocalIpAddressUtil {
    private static String localIP = resolveLocalIps().get(0);

    /**
     * 防止实例化
     */
    private LocalIpAddressUtil() {
    }

    /**
     * 获取第一个本机IP
     *
     * @return 本机IP
     */
    public static String getFirstLocalIP() {
        return localIP;
    }

    /**
     * 获取本地ip地址，有可能会有多个地址, 若有多个网卡则会搜集多个网卡的ip地址
     */
    public static List<InetAddress> resolveLocalAddresses() {
        List<InetAddress> addrs = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> ns = null;
        try {
            ns = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            // ignored...
        }
        while (ns != null && ns.hasMoreElements()) {
            NetworkInterface n = ns.nextElement();
            Enumeration<InetAddress> is = n.getInetAddresses();
            while (is.hasMoreElements()) {
                InetAddress i = is.nextElement();
                if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() && !i.isMulticastAddress()
                        && !isSpecialIp(i.getHostAddress())) {
                    addrs.add(i);
                }
            }
        }
        return addrs;
    }

    public static List<String> resolveLocalIps() {
        List<InetAddress> iNetAddresses = resolveLocalAddresses();
        List<String> ret = new ArrayList<>();
        for (InetAddress iNetAddress : iNetAddresses) {
            ret.add(iNetAddress.getHostAddress());
        }
        return ret;
    }

    private static boolean isSpecialIp(String ip) {
        return ip.contains(":") || ip.startsWith("127.") ||
                ip.startsWith("169.254.") || "255.255.255.255".equals(ip);

    }

    public static void main(String[] args) {
        System.out.println(LocalIpAddressUtil.getFirstLocalIP());
    }
}
