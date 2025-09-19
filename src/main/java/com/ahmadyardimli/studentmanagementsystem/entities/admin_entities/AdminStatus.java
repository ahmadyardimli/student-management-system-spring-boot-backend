package com.ahmadyardimli.studentmanagementsystem.entities.admin_entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_statuses")
public class AdminStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 20)
    private String status;

    public AdminStatus() {
    }

    public AdminStatus(String status) {
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
