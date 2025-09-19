package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;


public class StudentTypeDTO {
    private int id;
    private String type;

    public StudentTypeDTO() {
    }

    public StudentTypeDTO(int id, String type) {
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
