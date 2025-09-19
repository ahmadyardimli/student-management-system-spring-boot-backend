package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers;

import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;

public class FullTeacherRequest {
    private UserRequest userRequest;
    private TeacherRequest teacherRequest;

    // Default constructor
    public FullTeacherRequest() {
    }

    // Constructor with parameters
    public FullTeacherRequest(UserRequest userRequest, TeacherRequest teacherRequest) {
        this.userRequest = userRequest;
        this.teacherRequest = teacherRequest;
    }

    // Getters and setters
    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
    }

    public TeacherRequest getTeacherRequest() {
        return teacherRequest;
    }

    public void setTeacherRequest(TeacherRequest teacherRequest) {
        this.teacherRequest = teacherRequest;
    }
}
