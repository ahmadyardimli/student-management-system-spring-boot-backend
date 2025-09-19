package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class SubGroupDTO {
    private int id;
    private String subGroup;

    public SubGroupDTO() {
    }

    public SubGroupDTO(int id, String subGroup) {
        this.id = id;
        this.subGroup = subGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }
}
