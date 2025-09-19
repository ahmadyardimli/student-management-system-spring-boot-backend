package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details;

public class CategoryDTO {
    private int id;
    private String category;
    private int minClass;
    private int maxClass;

    public CategoryDTO() {
    }

    public CategoryDTO(int id, String category, int minClass, int maxClass) {
        this.id = id;
        this.category = category;
        this.minClass = minClass;
        this.maxClass = maxClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinClass() {
        return minClass;
    }

    public void setMinClass(int minClass) {
        this.minClass = minClass;
    }

    public int getMaxClass() {
        return maxClass;
    }

    public void setMaxClass(int maxClass) {
        this.maxClass = maxClass;
    }
}
