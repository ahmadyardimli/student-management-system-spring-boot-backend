package com.ahmadyardimli.studentmanagementsystem.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() { super("token_expired"); }
}
