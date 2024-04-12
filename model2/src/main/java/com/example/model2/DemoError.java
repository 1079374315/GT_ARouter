package com.example.model2;

public class DemoError extends Exception{

    public DemoError() {
    }

    public DemoError(String message) {
        super(message);
    }

    public DemoError(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoError(Throwable cause) {
        super(cause);
    }

    public DemoError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
