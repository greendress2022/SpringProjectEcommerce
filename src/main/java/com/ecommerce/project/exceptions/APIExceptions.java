package com.ecommerce.project.exceptions;

import java.io.Serial;

public class APIExceptions extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public APIExceptions() {
    }

    public APIExceptions(String message) {
        super(message);
    }
}
