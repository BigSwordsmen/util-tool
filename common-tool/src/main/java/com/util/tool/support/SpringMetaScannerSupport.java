/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.google.common.base.Predicate;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * 基于spring 容器，注解服务类
 * @author zhaoj
 * @version SpringMetaScannerSupport.java, v 0.1 2019-03-13 16:57
 */
public class SpringMetaScannerSupport {
    public static <A extends Annotation> void scanStaticClazz(String pkg, Class<A> annotationClz,
                                                              BiConsumer<A, Class> consumer) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(annotationClz, true);
        types.forEach(clz -> {
            A annotation = clz.getAnnotation(annotationClz);
            consumer.accept(annotation, clz);
        });
    }

    public static <A extends Annotation, O> void scanObjectMethod(O obj, Class<A> annotationClz,
                                                                  BiConsumer<A, Method> consumer) {
        Predicate<AnnotatedElement> p = ReflectionUtils.withAnnotation(annotationClz);
        Set<Method> methods = ReflectionUtils.getMethods(obj.getClass(), p);
        methods.forEach(m -> {
            A annotation = m.getAnnotation(annotationClz);
            consumer.accept(annotation, m);
        });
    }

    public static <A extends Annotation, O> void scanClassMethod(Class<? extends O> clz,
                                                                 Class<A> annotationClz, BiConsumer<A, Method> consumer) {
        Predicate<AnnotatedElement> p = ReflectionUtils.withAnnotation(annotationClz);
        Set<Method> methods = ReflectionUtils.getMethods(clz, p);
        methods.forEach(m -> {
            A annotation = m.getAnnotation(annotationClz);
            consumer.accept(annotation, m);
        });
    }

    public static <A extends Annotation> BeanPostProcessor newBeanMetaConsumerScanner(
            Class<A> annotationClz, Java8Support.TriConsumer<A, Object, Method> consumer) {
        return new BeanMethodMetaProcessor<>(consumer, annotationClz);
    }

    public static class BeanMethodMetaProcessor<A extends Annotation> implements BeanPostProcessor {

        Java8Support.TriConsumer<A, Object, Method> consumer;
        Class<A> annotationClz;

        public BeanMethodMetaProcessor(Java8Support.TriConsumer<A, Object, Method> consumer,
                                       Class<A> annotationClz) {
            this.consumer = consumer;
            this.annotationClz = annotationClz;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            scanObjectMethod(bean, annotationClz, (meta, method) -> {
                consumer.accept(meta, bean, method);
            });
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName)
                throws BeansException {
            return bean;
        }
    }
}
