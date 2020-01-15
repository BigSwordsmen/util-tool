/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 *
 * @author zhaoj
 * @version GuavaSupport.java, v 0.1 2019-03-13 14:11
 */
public class GuavaSupport {

    /**
     *
     * @param maximumSize
     * @param timeoutSeconds
     * @param getter
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> LoadingCache<K, V> guavaCache(int maximumSize, int timeoutSeconds,
                                                       ThrowableFunction<K, V, ? extends Exception> getter) {
        return CacheBuilder.newBuilder().maximumSize(maximumSize)
                .expireAfterWrite(timeoutSeconds, TimeUnit.SECONDS).build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        return getter.apply(key);
                    }
                });
    }

    public static <K, V> ConcurrentMap<K, V> simpleCache(int size, int timeout) {
        return expiringCache(null, size, 0, timeout, null);
    }

    public static <K, V> Function<K, V> simpleCacheWrapper(int size, int timeout, ThrowableFunction<K, V, ?> func) {
        LoadingCache<K, V> cache = guavaCache(size, timeout, (K key) -> {
            try {
                return func.apply(key);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        return (K k) -> {
            try {
                return cache.get(k);
            } catch (ExecutionException e) {
                Throwable ex = Supports.unwrap(e);
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * 基于guava cache的可过期、可设置cache
     *
     * @param name
     * @param maxSize
     * @param seconds
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentMap<K, V> expiringCacheByWrite(String name, int maxSize,
                                                                  int seconds) {
        return expiringCache(name, maxSize, 0, seconds, null);
    }

    /**
     * 基于访问超时的cache
     *
     * @param name
     * @param maxSize
     * @param seconds 超过seconds未访问之后过期
     * @param daemon
     * @return
     */
    public static <K, V> ConcurrentMap<K, V> expiringCacheByRead(String name, int maxSize,
                                                                 int seconds, RemovalListener<K, V> listener, boolean daemon) {
        ConcurrentMap<K, V> cache = expiringCache(name, maxSize, seconds, 0, listener);
        if (daemon) {
            int delay = seconds > 10 ? 10 : seconds;
            SCHEDULER.scheduleWithFixedDelay(() -> {
                try {
                    cache.put((K)NOT_EXIST_KEY, (V)NOT_EXIST_KEY);
                } catch (Exception e) {
                    // not happen
                }
            }, delay, delay, TimeUnit.SECONDS);
        }
        return cache;
    }

    public static <K, V> ConcurrentMap<K, V> expiringCache(String name, int maxSize,
                                                           int readSeconds, int writeSeconds, RemovalListener<K, V> listener) {
        CacheBuilder<K, V> builder =
                cacheBuilder(name, maxSize, readSeconds, writeSeconds, listener);
        return ((CacheBuilder)builder).build().asMap();
    }

    /**
     * http://stackoverflow.com/questions/10144194/how-does-guava-expire-entries-in-its-cachebuilder
     *
     * @param name
     * @param maxSize
     * @param readSeconds
     * @param writeSeconds
     * @param listener
     * @param <K>
     * @param <V>
     * @return
     */
    protected static <K, V> CacheBuilder<K, V> cacheBuilder(String name, int maxSize,
                                                            int readSeconds, int writeSeconds, RemovalListener<K, V> listener) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (!Strings.isNullOrEmpty(name)) {
            // builder.refreshAfterWrite();
        }
        if (readSeconds > 0) {
            builder.expireAfterAccess(readSeconds, TimeUnit.SECONDS);
        }
        if (writeSeconds > 0) {
            builder.expireAfterWrite(writeSeconds, TimeUnit.SECONDS);
        }
        if (maxSize > 0) {
            builder.maximumSize(maxSize);
        }
        if (listener != null) {
            builder.removalListener(listener);
        }
        return (CacheBuilder)builder;
    }

    private static final Object NOT_EXIST_KEY = new Object();

    private static final ScheduledExecutorService SCHEDULER = ThreadSupport.newScheduledThreadPool("cacheEvict", 1);
}
