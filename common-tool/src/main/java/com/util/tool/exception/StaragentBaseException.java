/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.exception;

/**
 *
 * @author zhaoj
 * @version StaragentBaseException.java, v 0.1 2019-03-13 17:29
 */
public class StaragentBaseException extends RuntimeException {
    public StaragentBaseException() {
        super();
    }

    public StaragentBaseException(String message) {
        super(message);
    }

    public StaragentBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaragentBaseException(Throwable cause) {
        super(cause);
    }

    public StaragentBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
