package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;

import java.util.List;

public class TeacherDTO {
    private int id;
    private String name;
    private String surname;
    private List<Integer> subject_ids;
    private int userId;
    private TeacherCommunicationSenderStatusDTO communicationSenderStatus;
    private List<String> subjectNames;
    private UserDTO user;

    public TeacherDTO() {
    }
    public TeacherDTO(int id, String name, String surname, List<Integer> subject_ids, int userId, TeacherCommunicationSenderStatusDTO communicationSenderStatus, List<String> subjectNames, UserDTO user) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.subject_ids = subject_ids;
        this.userId = userId;
        this.communicationSenderStatus = communicationSenderStatus;
        this.subjectNames = subjectNames;
        this.user = user;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public TeacherCommunicationSenderStatusDTO getCommunicationSenderStatus() {
        return communicationSenderStatus;
    }

    public void setCommunicationSenderStatus(TeacherCommunicationSenderStatusDTO communicationSenderStatus) {
        this.communicationSenderStatus = communicationSenderStatus;
    }

    public List<Integer> getSubject_ids() {
        return subject_ids;
    }

    public void setSubject_ids(List<Integer> subject_ids) {
        this.subject_ids = subject_ids;
    }

    public List<String> getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(List<String> subjectNames) {
        this.subjectNames = subjectNames;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}

