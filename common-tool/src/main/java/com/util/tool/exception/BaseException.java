/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.util.tool.exception;

import java.text.MessageFormat;

/**
 * @author zhaoj
 * @version BaseException.java, v 0.1 2018-09-19 14:13
 */
public abstract class BaseException extends RuntimeException {
    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected String code;

    /**
     * 异常构造器
     *
     * @param code      代码
     * @param msgFormat 消息模板,内部会用MessageFormat拼接，模板类似：userid={0},message={1},date{2}
     * @param args      具体参数的值
     */
    public BaseException(String code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }


    /**
     * 默认构造器
     */
    public BaseException() {
        super();
    }

    /**
     * 异常构造器
     *
     * @param message
     * @param cause
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 异常构造器
     *
     * @param cause
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 异常构造器
     *
     * @param message
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * 实例化异常
     *
     * @return 异常类
     */
    public abstract BaseException newInstance(String msgFormat, Object... args);

    /**
     * 错误消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 错误代码
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 重写堆栈填充，不填充错误堆栈信息
     *
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "[" + this.code + "]" + this.msg;
    }

}


