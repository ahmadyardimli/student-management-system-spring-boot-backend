package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class ClassNumberDTO {
    private int id;
    private int numberValue;

    public ClassNumberDTO() {
    }

    public ClassNumberDTO(int id, int numberValue) {
        this.id = id;
        this.numberValue = numberValue;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getNumberValue() {
        return numberValue;
    }
    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }
}
