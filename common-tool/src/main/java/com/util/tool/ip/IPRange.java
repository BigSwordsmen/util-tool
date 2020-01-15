/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.ip;

import com.util.exception.InvalidIPAddressException;
import com.util.exception.InvalidIPRangeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.RuntimeErrorException;

/**
 *
 * @author zhaoj
 * @version IPRange.java, v 0.1 2019-03-13 10:39
 */
public class IPRange implements Comparable<IPRange> {

    public IPRange(String range) throws InvalidIPRangeException {
        parseRange(range);
    }

    public IPRange(IPAddress ipAddress, int extendedNetworkPrefix) {
        this.ipAddress = ipAddress;
        this.extendedNetworkPrefix = extendedNetworkPrefix;
    }

    private void parseRange(String range) throws InvalidIPRangeException {
        if (StringUtils.isBlank(range)) {
            throw new InvalidIPRangeException();
        }
        int index = range.indexOf("/");
        if (index == -1) {
            throw new InvalidIPRangeException(range);
        }
        try {
            ipAddress = new IPAddress(range.substring(0, index));
        } catch (InvalidIPAddressException ex) {
            throw new InvalidIPRangeException(range);
        }
        String subnetStr = range.substring(index + 1);
        try {
            extendedNetworkPrefix = Integer.parseInt(subnetStr);
        } catch (Exception ex) {
            throw new InvalidIPRangeException(range);
        }
        if (StringUtils.isBlank(subnetStr) || extendedNetworkPrefix < 0) {
            throw new InvalidIPRangeException();
        }
        if (ipAddress.getIpVersion() == IPAddress.IPv4 && extendedNetworkPrefix > 32) {
            throw new InvalidIPRangeException();
        }
        if (ipAddress.getIpVersion() == IPAddress.IPv6 && extendedNetworkPrefix > 128) {
            throw new InvalidIPRangeException();
        }
    }

    public boolean isIPAddressInRange(IPAddress address) {
        String result1 = StringUtils.substring(ipAddress.toBinaryString(), 0, extendedNetworkPrefix);
        String result2 = StringUtils.substring(address.toBinaryString(), 0, extendedNetworkPrefix);
        return result1.endsWith(result2);
    }

    /**
     * 检查该range是否包含other
     *
     * @param other
     * @return
     */
    public boolean isRangeContainOther(IPRange other) {
        if (null == other) {
            return false;
        }
        if (other.getExtendedNetworkPrefix() <= getExtendedNetworkPrefix()) {
            return false;
        }
        return isIPAddressInRange(other.getIPAddress());
    }

    @Override
    public String toString() {
        return ipAddress + "/" + extendedNetworkPrefix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + extendedNetworkPrefix;
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
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
        IPRange other = (IPRange) obj;
        if (extendedNetworkPrefix != other.extendedNetworkPrefix) {
            return false;
        }
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        return true;
    }

    public IPAddress getIPAddress() {
        return ipAddress;
    }

    public int getExtendedNetworkPrefix() {
        return extendedNetworkPrefix;
    }

    @Override
    public int compareTo(IPRange o) {
        // bugfix o instanceof IPRange
        if (o != null) {
            return ((IPRange) o).getExtendedNetworkPrefix() - getExtendedNetworkPrefix();
        } else {
            LOG.error("ip is not iprange, it is null" + o);
            throw new RuntimeErrorException(null, "");
        }
    }

    private final static Log LOG = LogFactory.getLog(IPRange.class);
    private IPAddress ipAddress;
    private int extendedNetworkPrefix = 0;

}
