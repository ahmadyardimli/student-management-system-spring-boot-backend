package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;

public class CategoryRequest {
    private String category;
    private int minClass;
    private int maxClass;

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
        checkIfClassNumberIsValid(minClass, "minClass");
        this.minClass = minClass;
    }

    public int getMaxClass() {
        return maxClass;
    }

    public void setMaxClass(int maxClass) {
        checkIfClassNumberIsValid(maxClass, "maxClass");
        this.maxClass = maxClass;
    }

    private void checkIfClassNumberIsValid(int classNum, String classType){
        if (classNum < 1 || classNum > 11) {
            throw new RequestValidationException(classType + " 1 və 11 arasında olmalıdır.");
        }
    }
}
