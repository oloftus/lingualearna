package com.lingualearna.web.translation.provider;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class WrappedGoogleJsonResponseException extends Exception {

	private static final long serialVersionUID = -8059285450774778371L;

	public WrappedGoogleJsonResponseException() {

		super();
	}

	public WrappedGoogleJsonResponseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WrappedGoogleJsonResponseException(String message, Throwable cause) {

		super(message, cause);
	}

	public WrappedGoogleJsonResponseException(String message) {

		super(message);
	}

	public WrappedGoogleJsonResponseException(Throwable cause) {

		super(cause);
	}

	public int getStatusCode() {

		return getRootGoogleJsonResponseException().getStatusCode();
	}

	public GoogleJsonResponseException getRootGoogleJsonResponseException() {

		GoogleJsonResponseException rootException = (GoogleJsonResponseException) this.getCause();
		return rootException;
	}
}