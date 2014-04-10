package com.lingualearna.web.controller.util;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lingualearna.web.controller.model.ConstraintViolations;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ConstraintViolations handleConstraintViolations(HttpServletRequest request,
            ConstraintViolationException exception) {

        ConstraintViolations response = new ConstraintViolations();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            if (violation.getPropertyPath() == null || violation.getPropertyPath().toString().isEmpty()) {
                response.addGlobalError(violation.getMessage());
            }
            else {
                response.addFieldError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return response;
    }
}
