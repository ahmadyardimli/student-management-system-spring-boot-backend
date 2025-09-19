package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students;

import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;

public class FullStudentRequest {
    private UserRequest userRequest;
    private StudentRequest studentRequest;

    // Default constructor
    public FullStudentRequest() {
    }

    // Constructor with parameters
    public FullStudentRequest(UserRequest userRequest, StudentRequest studentRequest) {
        this.userRequest = userRequest;
        this.studentRequest = studentRequest;
    }

    // Getters and setters
    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
    }

    public StudentRequest getStudentRequest() {
        return studentRequest;
    }

    public void setStudentRequest(StudentRequest studentRequest) {
        this.studentRequest = studentRequest;
    }
}