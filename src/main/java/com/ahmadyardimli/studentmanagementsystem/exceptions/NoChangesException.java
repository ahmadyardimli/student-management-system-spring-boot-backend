package com.ahmadyardimli.studentmanagementsystem.exceptions;

public class NoChangesException extends RuntimeException {
    public NoChangesException() {
        super("No changes were made.");
    }
    public NoChangesException(String message) {
        super(message);
    }
}
