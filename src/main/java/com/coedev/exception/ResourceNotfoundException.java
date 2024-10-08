package com.coedev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotfoundException extends RuntimeException{

    public ResourceNotfoundException(String message) {
        super(message);
    }
}
