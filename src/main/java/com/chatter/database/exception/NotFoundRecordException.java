package com.chatter.database.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundRecordException extends RuntimeException {

    public NotFoundRecordException(String message) {
        super(message);
    }
}
