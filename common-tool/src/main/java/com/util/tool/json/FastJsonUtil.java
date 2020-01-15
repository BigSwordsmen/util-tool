/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.json;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author zhaoj
 * @version FastJsonUtil.java, v 0.1 2019-04-17 11:37
 */
public class FastJsonUtil {

    public static String bean2Json(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        return JSON.parseObject(jsonStr, objClass);
    }
}
