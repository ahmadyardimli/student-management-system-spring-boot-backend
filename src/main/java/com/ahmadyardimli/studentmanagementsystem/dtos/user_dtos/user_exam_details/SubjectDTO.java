package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class SubjectDTO {
    private int id;
    private String subject;

    public SubjectDTO() {
    }

    public SubjectDTO(int id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}