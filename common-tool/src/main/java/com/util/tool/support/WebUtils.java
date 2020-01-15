/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.util.tool.exception.StaragentBaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author zhaoj
 * @version WebUtils.java, v 0.1 2019-03-13 17:09
 */
public class WebUtils {
    /**
     * 添加cookies
     *
     * @param key
     * @param value
     * @param activeSecond
     * @param response
     */
    public static void addCookie(String key, String value, int activeSecond, HttpServletResponse response) {
        addCookie(null, key, value, activeSecond, response);
    }


    /**
     * 删除cookie
     *
     * @param key
     * @param response
     */
    public static void removeCookie(String key, HttpServletResponse response) {
        addCookie(null, key, "", 0, response);
    }

    public static void addCookie(String domain, String key, String value, int activeSecond, HttpServletResponse response) {
        Assert.hasText(key, "key can't be null");
        Assert.notNull(response, "response can't be null");
        try {
            Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath("/");
            if (StringUtils.isNotBlank(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setMaxAge(activeSecond);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new StaragentBaseException(e);
        }
    }

    /**
     * 获取cookie 值
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (StringUtils.equals(c.getName(), key)) {
                    try {
                        if (c.getValue() != null) {
                            return URLDecoder.decode(c.getValue(), "UTF-8");
                        }
                        return c.getValue();
                    } catch (UnsupportedEncodingException e) {
                        throw new StaragentBaseException(e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Thread-local绑定，只有在HttpServletResponse处理中可用
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    /**
     * Thread-local绑定，只有在HttpServletRequest处理中可用
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
