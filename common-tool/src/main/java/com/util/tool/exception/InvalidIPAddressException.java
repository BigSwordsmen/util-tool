package com.util.tool.exception;

/**
 * invalid ip address exeception
 * 
 * @author caojh
 *
 */
public class InvalidIPAddressException extends RuntimeException {
	private static final long serialVersionUID = -6146967309204054831L;

	public InvalidIPAddressException() {
		super();
	}

	public InvalidIPAddressException(String message) {
		super(message);
	}

	public InvalidIPAddressException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidIPAddressException(Throwable cause) {
		super(cause);
	}

}
