/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 线程\线程池\并发相关的辅助方法
 * @author zhaoj
 * @version ThreadSupport.java, v 0.1 2019-03-13 14:14
 */
public class ThreadSupport {
    public static ExecutorService finiteIO(ThreadFactory threadFactory, int timeout, int max) {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(max, max, timeout, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(), threadFactory);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * 返回适用于IO密集型的有限线程池
     *
     * @param group
     * @param max
     * @return
     */
    public static ExecutorService finiteIO(String group, int max) {
        final ThreadFactory factory =
                new ThreadFactoryBuilder().setNameFormat(group + "-%d").build();
        return finiteIO(factory, 60, max);
    }

    public static ExecutorService infiniteIO(String group) {
        final ThreadFactory factory =
                new ThreadFactoryBuilder().setNameFormat(group + "-%d").build();
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                factory);
    }

    public static ExecutorService finiteIO(String group, int timeout, int max) {
        final ThreadFactory factory =
                new ThreadFactoryBuilder().setNameFormat(group + "-%d").build();
        return finiteIO(factory, timeout, max);
    }

    public static ThreadFactory newThreadFactory(String group, boolean daemon, Integer priority) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder().setNameFormat(group + "-%d");
        if (daemon) {
            builder.setDaemon(true);
        }
        if (priority != null) {
            builder.setPriority(priority);
        }
        return builder.build();
    }

    private static final ThreadFactory DEFAULT_FACTORY = new ThreadFactoryBuilder().setNameFormat("default-%d").build();

    /**
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), DEFAULT_FACTORY);
    }

    /**
     * @param nThreads
     * @param threadFactory
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
    }

    /**
     * @param corePoolSize
     * @param threadFactory
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
    }

    public static ScheduledExecutorService newScheduledThreadPool(String name, int corePoolSize) {
        ThreadFactory factory = ThreadSupport.newThreadFactory(name, true, null);
        return newScheduledThreadPool(corePoolSize, factory);
    }

    public static void watchFutureTimeout(int millis, CompletableFuture<?> future, String errorMsg) {
        watchFutureTimeout(DEFAULT_SCHEDULER, millis, future, () -> {
            future.completeExceptionally(new TimeoutException(errorMsg));
        });
    }

    /**
     * 监视future是否超时
     *
     * @param scheduler
     * @param millis
     * @param future
     * @param timeoutHandler
     */
    public static void watchFutureTimeout(ScheduledExecutorService scheduler, int millis, CompletableFuture<?> future,
                                          Runnable timeoutHandler) {
        if (millis > 0 && !future.isDone()) {
            scheduler.schedule(() -> {
                if (!future.isDone()) {
                    timeoutHandler.run();
                }
            }, millis, TimeUnit.MILLISECONDS);
        }
    }

    private final static ScheduledExecutorService DEFAULT_SCHEDULER = new ScheduledThreadPoolExecutor(1,
            DEFAULT_FACTORY);
}
