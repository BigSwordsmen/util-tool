/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.json;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Map;

/**
 * 测试类
 * @author zhaoj
 * @version JsonObject.java, v 0.1 2019-03-13 11:53
 */
public class JsonObject {
    private String key;
    private Integer number;
    private Date date;
    private Map<String,String> map;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter method for property <tt>map</tt>.
     *
     * @return property value of map
     */
    public Map<String, String> getMap() {
        return map;
    }

    /**
     * Setter method for property <tt>map</tt>.
     *
     * @param map value to be assigned to property map
     */
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
