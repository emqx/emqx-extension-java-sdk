package io.emqx.extension.exceptions;

public class InvalidParameterException extends Exception {

	private static final long serialVersionUID = 7022252705650197633L;
	
	public InvalidParameterException(String message) {
		super(message);
	}

}
