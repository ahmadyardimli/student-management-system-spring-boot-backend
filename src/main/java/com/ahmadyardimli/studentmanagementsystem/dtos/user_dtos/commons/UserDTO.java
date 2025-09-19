package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons;

public class UserDTO {
    private int id;
    private String username;
    private String email;
    private UserTypeDTO userType;
    private UserStatusDTO status;
    private String createdAt;
    private String updatedAt;
    private String authKey;
    private String verificationToken;


    public UserDTO() {
    }

    public UserDTO
            (int id, String username, String email, UserTypeDTO userType, UserStatusDTO status, String createdAt, String updatedAt, String password, int userTypeId, String authKey, String verificationToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authKey = authKey;
        this.verificationToken = verificationToken;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserTypeDTO getUserType() {
        return userType;
    }

    public void setUserType(UserTypeDTO userType) {
        this.userType = userType;
    }

    public UserStatusDTO getStatus() {
        return status;
    }

    public void setStatus(UserStatusDTO status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

}
