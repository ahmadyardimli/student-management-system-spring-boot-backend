package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;


public class SectionDTO {
    private int id;
    private String section;

    public SectionDTO() {
    }

    public SectionDTO(int id, String section) {
        this.id = id;
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
