package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers;

import java.util.List;

public class TeacherRequest {
    private String name;
    private String surname;
    private List<Integer> subject_ids;
    private int userId;
    private int communicationSenderStatusId;

    public TeacherRequest() {
    }

    public TeacherRequest(String name, String surname, List<Integer> subjectIds, int userId, int communicationSenderStatusId) {
        this.name = name;
        this.surname = surname;
        this.subject_ids = subjectIds;
        this.userId = userId;
        this.communicationSenderStatusId = communicationSenderStatusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommunicationSenderStatusId() {
        return communicationSenderStatusId;
    }

    public void setCommunicationSenderStatusId(int communicationSenderStatusId) {
        this.communicationSenderStatusId = communicationSenderStatusId;
    }

    public List<Integer> getSubject_ids() {
        return subject_ids;
    }

    public void setSubject_ids(List<Integer> subject_ids) {
        this.subject_ids = subject_ids;
    }
}
