package com.ahmadyardimli.studentmanagementsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SameUserIdsException extends RuntimeException {
    public SameUserIdsException(String message) {
        super(message);
    }
}