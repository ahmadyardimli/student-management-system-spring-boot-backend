package com.ahmadyardimli.studentmanagementsystem.entities.admin_entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "admin_roles")
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role", nullable = false, unique = true)
    private String role;

    @OneToMany(mappedBy = "adminRole", cascade = CascadeType.MERGE)
    private List<Admin> admins;

    public AdminRole() {
    }

    public AdminRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
}
