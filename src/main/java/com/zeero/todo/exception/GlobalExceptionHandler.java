package com.zeero.todo.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String FAILED_STATUS = "FAILED";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(STATUS, FAILED_STATUS);
        errors.put(ERROR, ex.getLocalizedMessage());
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(STATUS, FAILED_STATUS);
        errors.put(ERROR, ex.getLocalizedMessage());
        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(STATUS, FAILED_STATUS);
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(ERROR, error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidEnumValue(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(STATUS, FAILED_STATUS);
        if (ex.getCause() instanceof InvalidFormatException cause) {
            cause = (InvalidFormatException) ex.getCause();
                errors.put(ERROR, "Invalid "+ cause.getTargetType().getSimpleName() +" value");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errors);
        }
        errors.put(ERROR, "Invalid request data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
