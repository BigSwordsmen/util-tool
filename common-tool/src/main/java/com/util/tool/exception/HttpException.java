package com.util.tool.exception;

/**
 * @author wanqiang.mwq
 * @date 2017/9/14
 */
public class HttpException extends RuntimeException {

    /**
     * http 响应码
     */
    private int httpStatus;
    /**
     * 异常信息
     */
    private String errorMsg;

    public HttpException(int httpStatus, String errorMsg) {
        this(httpStatus, errorMsg, null);
    }

    public HttpException(int httpStatus, String errorMsg, Throwable cause) {
        super("httpStatus=" + httpStatus + " errorMsg=" + errorMsg + ";", cause);
        this.httpStatus = httpStatus;
        this.errorMsg = errorMsg;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
