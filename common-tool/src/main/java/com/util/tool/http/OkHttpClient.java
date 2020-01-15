/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.http;


import com.util.tool.exception.HttpException;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 简单封装
 * @author zhaoj
 * @version OkHttpClient.java, v 0.1 2019-03-13 11:07
 */
public class OkHttpClient {
    private static okhttp3.OkHttpClient httpClient = null;

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    static {
        ConnectionPool pool = new ConnectionPool(100, 1, TimeUnit.MINUTES);
        httpClient = new okhttp3.OkHttpClient.Builder()
                .connectionPool(pool)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
    }

    private static Request buildGetRequest(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(url);
        if (params != null && params.size() > 0) {
            urlStringBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    urlStringBuilder.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(StringUtils.trimToEmpty(entry.getValue()), "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    throw new HttpException(400, e.getMessage(), e);
                }
            }
            urlStringBuilder.deleteCharAt(urlStringBuilder.length() - 1);
        }
        Request.Builder builder = new Request.Builder().url(urlStringBuilder.toString());

        //添加header
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        return builder.get().build();
    }

    /**
     * get
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params) {
        return executeRequest(buildGetRequest(url, params, null));
    }

    /**
     * 异步方式请求get
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void get(String url, Map<String, String> params, Callback callback) {
        executeRequest(buildGetRequest(url, params, null), callback);
    }

    /**
     * get
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        return executeRequest(buildGetRequest(url, params, headers));
    }

    /**
     * post
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String post(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(StringUtils.trimToEmpty(entry.getKey()), StringUtils.trimToEmpty(entry.getValue()));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return executeRequest(request);
    }

    /**
     * post
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(StringUtils.trimToEmpty(entry.getKey()), StringUtils.trimToEmpty(entry.getValue()));
            }
        }
        Request.Builder postBuilder = new Request.Builder().url(url).post(builder.build());

        //添加header
        if (headers != null && headers.size() != 0) {
            for (String key : headers.keySet()) {
                postBuilder.addHeader(key, headers.get(key));
            }
        }

        Request request = postBuilder.build();
        return executeRequest(request);
    }

    /**
     * post json
     *
     * @param url
     * @param json
     * @return
     */
    public static String postJson(String url, String json) {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return executeRequest(request);
    }

    public static String executeRequest(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new HttpException(response.code(), response.body().string());
            }
        } catch (IOException e) {
            throw new HttpException(502, e.getMessage(), e);
        }
    }

    public static void executeRequest(Request request, Callback callback) {
        httpClient.newCall(request).enqueue(callback);
    }
}
