package com.tailoredshapes.underbar.exceptions;

public class UnderBarred extends RuntimeException {
    public UnderBarred() {}

    public UnderBarred(String message) {
        super(message);
    }

    public UnderBarred(String message, Throwable cause) {
        super(message, cause);
    }

    public UnderBarred(Throwable cause) {
        super(cause);
    }

    public UnderBarred(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
