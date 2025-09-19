package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons;


public class UserRequest {
    private String username;
    private String password;
    private String email;
    private int userTypeId;
    private int statusId = -1;

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int status) {
        this.statusId = status;
    }
}


