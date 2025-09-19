package com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos;

public class AdminRoleDTO {
    private int id;
    private String role;

    public AdminRoleDTO() {
        // Default constructor
    }

    public AdminRoleDTO(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
