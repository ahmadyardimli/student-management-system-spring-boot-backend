package com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos;

import java.time.LocalDateTime;

public class AdminDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private LocalDateTime lastLogin;
    private int loginAttempts;
    private AdminRoleDTO adminRole;
    private AdminStatusDTO adminStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdminDTO() {
        // Default constructor
    }

    // Constructors, getters, and setters

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

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public AdminRoleDTO getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRoleDTO adminRole) {
        this.adminRole = adminRole;
    }

    public AdminStatusDTO getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatusDTO adminStatus) {
        this.adminStatus = adminStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
