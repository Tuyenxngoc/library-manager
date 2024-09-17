package com.example.librarymanager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorizedException extends RuntimeException {

    private Object[] params;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Object... params) {
        super(message);
        this.params = params;
    }

}