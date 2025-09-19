package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students;


import jakarta.persistence.*;

@Entity
@Table(name = "student_communication_sender_statuses")
public class StudentCommunicationSenderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 20)
    private String status;

    public StudentCommunicationSenderStatus() {
    }

    public StudentCommunicationSenderStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
