package com.ahmadyardimli.studentmanagementsystem.utils;

public final class TokenHash {
    private TokenHash() {}
    public static String sha256Hex(String raw) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) sb.append(String.format("%02X", b));
            return sb.toString(); // 64-char upper-hex
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

