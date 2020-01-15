/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.regex;

import cn.hutool.core.util.ReUtil;

/**
 * @author zhaoj
 * @version RegexUtil.java, v 0.1 2019-04-12 16:56
 */
public class RegexUtil {
    /**
     * 手机号码
     */
    public static final String REGEX_MOBILE_1 = "^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|19[0-9]|14[57])[0-9]{8}$";

    /**
     * 支持+86 86 86- 等多种组合:^(((\+86)|(\+86-))|((86)|(86\-))|((0086)|(0086\-)))?1[3|5|7|8]\d{9}$
     */
    public static final String REGEX_MOBILE_2 = "^((\\+86|\\+86\\-)|(86|86\\-)|(0086|0086\\-))?1[3|5|7|8]\\d{9}$";

    /**
     * 验证带区号的座机
     */
    public static final String REGEX_PHONE_1 = "^[0][1-9]{2,3}-[0-9]{5,10}$";

    /**
     * 验证没有区号的座机
     */
    public static final String REGEX_PHONE_2 = "^[1-9]{1}[0-9]{5,8}$";

    /**
     * 区号+座机号码+分机号码:"^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,6})?$"; 包含区号和没有区号的座机
     */
    public static final String REGEX_PHONE_3 = "^(0\\d{2,3}\\-)?([2-9]\\d{6,7})+(\\-\\d{1,6})?$";

    /**
     * 手机号码验证
     *
     * @param input 待验证文本
     * @return
     */
    public static boolean isMobile1(String input) {
        return isMatch(REGEX_MOBILE_1, input);
    }

    /**
     * 支持+86 86 86- 等多种组合
     * @param input
     * @return
     */
    public static boolean isMobile2(String input) {
        return isMatch(REGEX_MOBILE_2, input);
    }

    /**
     * 区号+座机号码+分机号码
     * @param input
     * @return
     */
    public static boolean isPhone3(String input) {
        return isMatch(REGEX_PHONE_3, input);
    }
    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, String input) {
        return input != null && input.length() > 0 && ReUtil.isMatch(regex, input);
    }

    public static void main(String[] args) {
        String phone = "8888888";
        System.out.println(isPhone3(phone));
    }
}
