package com.ahmadyardimli.studentmanagementsystem.requests.admin_requests;

public class AdminRequest{
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private int adminRoleId;
    private int adminStatusId;

    public AdminRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(int adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    public int getAdminStatusId() {
        return adminStatusId;
    }

    public void setAdminStatusId(int adminStatusId) {
        this.adminStatusId = adminStatusId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
