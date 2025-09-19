package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons;

public class UserTypeDTO {
    private int id;
    private String type;

    public UserTypeDTO() {
    }

    public UserTypeDTO(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
