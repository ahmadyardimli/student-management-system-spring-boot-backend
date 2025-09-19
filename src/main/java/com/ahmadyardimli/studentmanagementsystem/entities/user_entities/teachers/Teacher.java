package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;
    private String surname;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communication_sender_status_id")
    private TeacherCommunicationSenderStatus communicationSenderUserStatus;

    public Teacher() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public TeacherCommunicationSenderStatus getCommunicationSenderStatus() {
        return communicationSenderUserStatus;
    }

    public void setCommunicationSenderStatus(TeacherCommunicationSenderStatus communicationSenderUserStatus) {
        this.communicationSenderUserStatus = communicationSenderUserStatus;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
