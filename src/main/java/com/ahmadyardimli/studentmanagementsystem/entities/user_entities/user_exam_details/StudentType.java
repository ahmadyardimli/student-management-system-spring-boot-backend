package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;

import java.util.List; // Import List instead of Set

@Entity
@Table(name = "student_types")
public class StudentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "studentType", fetch = FetchType.LAZY)
    private List<Student> students;

    public StudentType(){}

    public StudentType(String type) {
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

    public List<Student> getStudents() { // Change the return type
        return students;
    }

    public void setStudents(List<Student> students) { // Change the parameter type
        this.students = students;
    }
}
