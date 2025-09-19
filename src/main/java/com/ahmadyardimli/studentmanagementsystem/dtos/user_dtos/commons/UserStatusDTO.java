package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons;

public class UserStatusDTO {
    private int id;
    private String status;

    public UserStatusDTO() {
    }

    public UserStatusDTO(int id, String name) {
        this.id = id;
        this.status = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

