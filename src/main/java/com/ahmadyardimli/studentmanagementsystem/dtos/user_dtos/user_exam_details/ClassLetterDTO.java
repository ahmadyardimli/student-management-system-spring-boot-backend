package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class ClassLetterDTO {
    private int id;
    private String letterValue;

    public ClassLetterDTO() {
    }

    public ClassLetterDTO(int id, String letterValue) {
        this.id = id;
        this.letterValue = letterValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetterValue() {
        return letterValue;
    }

    public void setLetterValue(String letterValue) {
        this.letterValue = letterValue;
    }
}
