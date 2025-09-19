package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_types")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String type;

    @OneToMany(mappedBy = "userType", cascade = CascadeType.MERGE)
    private List<User> users;

    public UserType() {
    }

    public UserType(String type) {
        this.type = type;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
