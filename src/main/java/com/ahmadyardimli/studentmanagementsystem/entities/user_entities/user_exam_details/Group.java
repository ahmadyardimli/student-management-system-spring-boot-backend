package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;


import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "students_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String group;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public Group(){}

    public Group(String group) {
        this.id = id;
        this.group = group;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
