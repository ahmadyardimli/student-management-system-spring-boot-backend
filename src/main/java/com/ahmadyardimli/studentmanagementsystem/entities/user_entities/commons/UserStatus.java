package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons;

import jakarta.persistence.*;

@Entity
@Table(name = "user_statuses")
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 20)
    private String status;

    public UserStatus() {
    }

    public UserStatus(String status) {
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
