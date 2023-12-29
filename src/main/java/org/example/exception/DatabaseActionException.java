package org.example.exception;

public class DatabaseActionException extends RuntimeException {
    public DatabaseActionException(String message, Throwable cause) {
        super(message,cause);
    }
}
