package com.util.tool.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonUtil {
    // fastjson 的序列化配置
    public final static SerializeConfig fastjson_serializeConfig_datetime = new SerializeConfig();

    // 默认打出所有属性(即使属性值为null)|属性排序输出,为了配合历史记录
    private final static SerializerFeature[] fastJsonFeatures                = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.SortField };

    static {
        fastjson_serializeConfig_datetime.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static <T> T parseObject(String item, Class<T> clazz) {
        if (StringUtils.isBlank(item)) {
            return null;
        }
        return JSON.parseObject(item, clazz);
    }

    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return JSON.parseArray(text, clazz);
    }

    public static JSONObject parseJSONObject(String text) {
        return JSON.parseObject(text);
    }

    public static JSONArray parseJSONArray(String text) {
        return JSON.parseArray(text);
    }

    public static String toJsonString(Object object) {
        return toJsonString(object, fastjson_serializeConfig_datetime);
    }

    public static String toJsonString(Object object, SerializeConfig serializeConfig) {
        if (null == object) {
            return "";
        }
        return JSON.toJSONString(object, serializeConfig, fastJsonFeatures);
    }

    public static void main(String[] args) {
        JsonObject obj = new JsonObject();
        obj.setDate(new Date());
        obj.setKey("key");
        obj.setNumber(1);
        Map<String,String> map = new HashMap<>();
        map.put("ooo",null);
        obj.setMap(map);
        String json = JsonUtil.toJsonString(obj);
        System.out.println("json:" + json);
        JsonObject parseObject = JsonUtil.parseObject(json, JsonObject.class);
        System.out.println("object:" + parseObject.toString());

        assertEquals(obj.getDate(), parseObject.getDate());
        assertEquals(obj.getKey(), parseObject.getKey());
        assertEquals(obj.getNumber(), parseObject.getNumber());
    }
}
