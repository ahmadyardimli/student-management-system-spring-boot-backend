package com.ahmadyardimli.studentmanagementsystem.exceptions;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException() { super("token_invalid"); }
}
