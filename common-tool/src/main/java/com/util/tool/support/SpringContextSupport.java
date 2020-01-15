/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zhaoj
 * @version SpringContextSupport.java, v 0.1 2019-03-13 16:53
 */
public class SpringContextSupport implements ApplicationContextAware {
    private static ApplicationContext ctx;

    public static ApplicationContext getContext() {
        return ctx;
    }

    public static <T> T getBean(Class<? extends T> clz) {
        return ctx.getBean(clz);
    }

    public ApplicationContext getApplicationContext() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> void autowireBean(T bean) {
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(bean);
    }

    public static  <T> T autowireBean(Class<T> beanClass) {
        AutowireCapableBeanFactory beanFactory =
                ctx.getAutowireCapableBeanFactory();
        try {
            T bean = beanClass.newInstance();
            autowireBean(bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> getBeanOfType(Class<T> clz, ApplicationContext ctx) {
        String[] names = ctx.getBeanNamesForType(clz);
        if (names == null || names.length == 0) {
            return Collections.emptyList();
        }
        List<T> beans = new ArrayList<>(names.length);
        for (String name : names) {
            Object bean = ctx.getBean(name);
            beans.add((T)bean);
        }
        return beans;
    }

    public static <T> List<T> getBeanOfType(Class<T> clz) {
        return getBeanOfType(clz, ctx);
    }

    public static <T> T getBeanByName(String name) {
        return getBeanByName(name, ctx);
    }

    public static <T> T getBeanByName(String name, ApplicationContext ctx) {
        Object bean = ctx.getBean(name);
        if (bean != null) {
            return ((T)bean);
        }
        return null;
    }
}
