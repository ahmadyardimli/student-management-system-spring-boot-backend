package com.ahmadyardimli.studentmanagementsystem.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {
    public static String generateUniqueAuthKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static String generateUniqueVerificationToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }
}
