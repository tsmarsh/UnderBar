package com.tailoredshapes.underbar.exceptions;

public class UnderBarred extends RuntimeException {

    public UnderBarred(String message) {
        super(message);
    }

    public UnderBarred(String message, Throwable cause) {
        super(message, cause);
    }
}
