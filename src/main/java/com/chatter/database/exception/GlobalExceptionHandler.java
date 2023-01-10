package com.chatter.database.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, Set<String>> fieldErrors = new HashMap<>();
        List<FieldError> fieldErrorList = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            if (fieldErrors.containsKey(fieldError.getField())) {
                fieldErrors.get(fieldError.getField()).add(fieldError.getDefaultMessage());
            } else {
                Set<String> errors = new HashSet<>();
                errors.add(fieldError.getDefaultMessage());
                fieldErrors.put(fieldError.getField(), errors);
            }
        }

        BadRequestResponse badRequestResponse = BadRequestResponse.builder()
                .message("Invalid data supplied")
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
    }

    @ExceptionHandler({DuplicateRecordException.class})
    public ResponseEntity<ErrorMessage> handleUserEmailDuplicated(DuplicateRecordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler({NotFoundRecordException.class})
    public ResponseEntity<ErrorMessage> handleUserEmailNotFound(DuplicateRecordException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
    }
}
