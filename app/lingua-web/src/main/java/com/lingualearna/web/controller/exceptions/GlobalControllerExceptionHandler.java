package com.lingualearna.web.controller.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.lingualearna.web.controller.model.ConstraintViolations;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    private void constructResponseFromViolations(Set<ConstraintViolation<?>> globalErrors,
            Set<ConstraintViolation<?>> fieldErrors,
            ConstraintViolations response) {

        for (ConstraintViolation<?> violation : globalErrors) {
            response.addGlobalError(violation.getMessage());
        }
        for (ConstraintViolation<?> violation : fieldErrors) {
            response.addFieldError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    private void deduplicateViolations(Set<ConstraintViolation<?>> globalErrors, Set<ConstraintViolation<?>> fieldErrors) {

        for (ConstraintViolation<?> fieldViolation : fieldErrors) {
            for (ConstraintViolation<?> globalViolation : globalErrors) {
                if (isDuplicateViolation(fieldViolation, globalViolation)) {
                    /*
                     * We only want to remove one instance as any others would
                     * be real global errors, so break after removing the first.
                     */
                    globalErrors.remove(globalViolation);
                    break;
                }
            }
        }
    }

    /**
     * Handles bean validation violations on controller models
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ConstraintViolations handleControllerModelConstraintViolations(HttpServletRequest request,
            MethodArgumentNotValidException exception) {

        ConstraintViolations response = new ConstraintViolations();
        BindingResult errors = exception.getBindingResult();

        for (FieldError error : errors.getFieldErrors()) {
            response.addFieldError(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : errors.getGlobalErrors()) {
            response.addGlobalError(error.getDefaultMessage());
        }

        return response;
    }

    /**
     * Handles bean validation violations on entities
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ConstraintViolations handleEntityConstraintViolations(HttpServletRequest request,
            ConstraintViolationException exception) {

        Set<ConstraintViolation<?>> globalViolations = new HashSet<>();
        Set<ConstraintViolation<?>> fieldViolations = new HashSet<>();
        ConstraintViolations response = new ConstraintViolations();

        splitGlobalAndFieldViolations(exception, globalViolations, fieldViolations);
        deduplicateViolations(globalViolations, fieldViolations);
        constructResponseFromViolations(globalViolations, fieldViolations, response);

        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericExceptions(HttpServletRequest request,
            Exception exception) {

        ModelAndView mv = new ModelAndView("error");
        StringWriter sw = new StringWriter();
        PrintWriter ps = new PrintWriter(sw);
        exception.printStackTrace(ps);
        mv.addObject("stacktrace", sw.toString());
        return mv;
    }

    /**
     * Handles custom validation exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ConstraintViolations handleValidationExceptions(HttpServletRequest request,
            ValidationException exception) {

        ConstraintViolations response = new ConstraintViolations();

        for (ValidationException.FieldError error : exception.getFieldErrors()) {
            response.addFieldError(error.getFieldName(), error.getErrorMessage());
        }
        for (String error : exception.getGlobalErrors()) {
            response.addGlobalError(error);
        }

        return response;
    }

    private boolean isDuplicateViolation(ConstraintViolation<?> fieldViolation, ConstraintViolation<?> globalViolation) {

        return globalViolation.getMessage().equals(fieldViolation.getMessage());
    }

    private void splitGlobalAndFieldViolations(ConstraintViolationException exception,
            Set<ConstraintViolation<?>> globalErrors,
            Set<ConstraintViolation<?>> fieldErrors) {

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            if (violation.getPropertyPath() == null || violation.getPropertyPath().toString().isEmpty()) {
                globalErrors.add(violation);
            }
            else {
                fieldErrors.add(violation);
            }
        }
    }
}
