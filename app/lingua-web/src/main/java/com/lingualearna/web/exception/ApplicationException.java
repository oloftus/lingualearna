package com.lingualearna.web.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -3662629702427541117L;

	public ApplicationException() {

	}

	public ApplicationException(String message) {

		super(message);
	}

	public ApplicationException(Throwable cause) {

		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {

		super(message, cause);
	}

	public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}
}