package com.ahmadyardimli.studentmanagementsystem.utils;

import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;

public class ValidationUtils {
    private ValidationUtils() {
    }
    // Validates that the input has no more than one consecutive space
    public static void validateSingleSpace(String input) {
        // Pattern to allow any characters with no more than one space between them
        if (!input.matches("^(?!.* {2,}).+$")) {
            throw new RequestValidationException("Only single spaces are allowed.");
        }
    }

    // Validates that the name or surname contains only letters, spaces, hyphens or apostrophes
    public static void validateNameOrSurname(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new RequestValidationException(fieldName + " cannot be empty.");
        }

        // the name only contains letters, single spaces, hyphens, or apostrophes
        if (!input.matches("^[\\p{L}\\s'-]+$")) {
            throw new RequestValidationException(fieldName + " must contain only letters, single spaces, hyphens, and apostrophes.");
        }
    }


    // Validates that the input contains only a single letter
    public static void validateSingleLetter(String input) {
        if (!input.matches("^[\\p{L}]$")) {
            throw new RequestValidationException("Class letter must be exactly one letter.");
        }
    }


    // Validates student code is exactly 7 digits
    public static void validateStudentCode(String studentCode) {
        if (studentCode == null || studentCode.trim().isEmpty()) {
            throw new RequestValidationException("Student code cannot be empty.");
        }

        if (!studentCode.matches("^\\d{7}$")) {
            throw new RequestValidationException("Student code must be exactly 7 digits.");
        }
    }

     // Validates mobile phone number contains only digits and optionally a leading '+'
    public static void validateMobilePhone(String mobilePhone) {
        // If mobile phone is optional in the system, allow null or empty
        if (mobilePhone == null || mobilePhone.trim().isEmpty()) {
            return;
        }

        // Phone number should start with optional '+' and contain only digits afterward
        if (!mobilePhone.matches("^\\+?\\d+$")) {
            throw new RequestValidationException("Mobile phone number may contain only digits and an optional leading '+'.");
        }
    }
}
