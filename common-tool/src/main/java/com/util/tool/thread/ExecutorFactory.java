/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zhaoj
 * @version ExecutorFactory.java, v 0.1 2019-03-13 10:28
 */
public class ExecutorFactory {
    /**
     * 创建无限队列非固定大小线程池
     * @param corePoolSize     核心线程数
     * @param maximumPoolSize  最大线程数
     * @param threadNamePrefix 线程名前缀
     * @return
     */
    public static ThreadPoolExecutor newExecutor(int corePoolSize,
                                                 int maximumPoolSize,
                                                 String threadNamePrefix) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryImpl(threadNamePrefix));
    }

    /**
     * 创建无限队列固定大小线程池
     * @param nThreads         固定线程数
     * @param threadNamePrefix 线程名前缀
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads, String threadNamePrefix) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryImpl(threadNamePrefix));
    }

    /**
     * @param corePoolSize
     * @param threadNamePrefix
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, String threadNamePrefix) {
        return new ScheduledThreadPoolExecutor(corePoolSize, new ThreadFactoryImpl(threadNamePrefix));
    }
}
