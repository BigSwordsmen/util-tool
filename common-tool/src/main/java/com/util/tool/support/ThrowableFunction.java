/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

/**
 *
 * @author zhaoj
 * @version ThrowableFunction.java, v 0.1 2019-03-13 14:12
 */
@FunctionalInterface
public interface ThrowableFunction<I, O, E extends Throwable> {
    O apply(I input) throws E;
}
