package com.lingualearna.web.translation;

public class TranslationException extends Exception {

	private static final long serialVersionUID = -4668256352536163633L;

	public TranslationException() {

	}

	public TranslationException(String message) {

		super(message);
	}

	public TranslationException(Throwable cause) {

		super(cause);
	}

	public TranslationException(String message, Throwable cause) {

		super(message, cause);
	}

	public TranslationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}
}
