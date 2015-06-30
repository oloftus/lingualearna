package com.lingualearna.web.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5171383721642359730L;

    public ResourceNotFoundException(String message) {

        super(message);
    }
}
