package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class ForeignLanguageDTO {
    private int id;
    private String foreignLanguage;

    public ForeignLanguageDTO() {
    }

    public ForeignLanguageDTO(int id, String foreignLanguage) {
        this.id = id;
        this.foreignLanguage = foreignLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(String foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

}
