package com.ahmadyardimli.studentmanagementsystem.responses;

public class AuthResponse {
    private String message;
    private int subjectId; // I use this name, because I have 2 separate entities: Admin (admin_id) and User (user_id).
    // I use one common name for id
    private String accessToken;
    private String refreshToken;
    private String  role;
    private String userType;

    public AuthResponse() {}
    public AuthResponse(String message, String accessToken, int subjectId, String role, String userType) {
        this.message = message;
        this.accessToken = accessToken;
        this.subjectId = subjectId;
        this.role = role;
        this.userType = userType;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
