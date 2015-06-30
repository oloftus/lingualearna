package com.lingualearna.web.shared.exceptions;

/**
 * Indicates some sort of misconfiguration in the system
 */
public class ConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 7593735663074498208L;

    public ConfigurationException(String message) {

        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {

        super(message, cause);
    }
}
