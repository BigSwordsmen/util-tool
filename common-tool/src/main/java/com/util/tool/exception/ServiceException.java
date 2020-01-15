package com.util.tool.exception;

public class ServiceException extends Exception {
	
	private String code;
	
	private String msg;
	
	public ServiceException(String code) {
		super(code);
		this.code = code;
	}

	public ServiceException(String code,String msg) {
		super(code+"["+msg+"]");
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
