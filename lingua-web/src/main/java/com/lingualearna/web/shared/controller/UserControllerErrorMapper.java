package com.lingualearna.web.shared.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.lingualearna.web.security.users.UserException;
import com.lingualearna.web.shared.validation.ValidationException;

@Component
public class UserControllerErrorMapper {

    public void populateBindingResultFromException(Exception exception, BindingResult bindingResult, String modelName) {

        if (exception instanceof UserException) {
            UserException userCreationException = (UserException) exception;
            populateBindingResultFromUserCreationException(bindingResult, userCreationException, modelName);
        }

        else if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
            populateBindingResultFromCve(bindingResult, constraintViolationException, modelName);
        }

        else if (exception instanceof ValidationException) {
            ValidationException validationException = (ValidationException) exception;
            populateBindingResultFromValidationException(bindingResult, validationException, modelName);
        }
    }

    private void populateBindingResultFromCve(BindingResult bindingResult,
            ConstraintViolationException constraintViolationException, String modelName) {

        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        for (ConstraintViolation<?> violation : constraintViolations) {
            Path propertyPath = violation.getPropertyPath();
            String errorMessage = violation.getMessage();

            ObjectError error;
            if (propertyPath == null) {
                error = new ObjectError(modelName, errorMessage);
            }
            else {
                error = new FieldError(modelName, propertyPath.toString(), errorMessage);
            }
            bindingResult.addError(error);
        }
    }

    private void populateBindingResultFromUserCreationException(BindingResult bindingResult,
            UserException userCreationException, String modelName) {

        for (String problem : userCreationException.getProblems()) {
            ObjectError error = new ObjectError(modelName, problem);
            bindingResult.addError(error);
        }
    }

    private void populateBindingResultFromValidationException(BindingResult bindingResult,
            ValidationException validationException, String userSignupModel) {

        Map<String, List<String>> fieldErrors = validationException.getFieldErrors();
        List<String> globalErrors = validationException.getGlobalErrors();

        for (String fieldName : fieldErrors.keySet()) {
            List<String> errors = fieldErrors.get(fieldName);
            for (String errorMessage : errors) {
                ObjectError error = new FieldError(userSignupModel, fieldName, errorMessage);
                bindingResult.addError(error);
            }
        }

        for (String errorMessage : globalErrors) {
            ObjectError error = new ObjectError(userSignupModel, errorMessage);
            bindingResult.addError(error);
        }
    }
}
