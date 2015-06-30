package com.lingualearna.web.shared.exceptions;

/**
 * Indicates a problem that is out of the scope of the normal operation of the
 * program, e.g. an unknown API error response but that is not fatal for the
 * system.
 */
public class UnexpectedProblemException extends RuntimeException {

    private static final long serialVersionUID = -3662629702427541117L;

    public UnexpectedProblemException(String message) {

        super(message);
    }

    public UnexpectedProblemException(String message, Throwable cause) {

        super(message, cause);
    }
}
