package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class GroupDTO {
    private int id;
    private String group;

    public GroupDTO() {
    }

    public GroupDTO(int id, String group) {
        this.id = id;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
