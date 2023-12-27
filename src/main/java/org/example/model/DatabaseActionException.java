package org.example.model;

public class DatabaseActionException extends RuntimeException {
    public DatabaseActionException(String message, Throwable cause) {
        super(message,cause);
    }
}
