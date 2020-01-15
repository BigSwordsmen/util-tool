package com.util.tool.exception;

public class InvalidIPRangeException extends RuntimeException {

	public InvalidIPRangeException() {
		super();
	}

	public InvalidIPRangeException(String message) {
		super(message);
	}

	public InvalidIPRangeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidIPRangeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5712822213147464849L;

}
