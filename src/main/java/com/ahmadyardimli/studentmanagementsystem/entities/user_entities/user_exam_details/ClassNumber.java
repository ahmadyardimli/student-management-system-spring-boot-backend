package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "class_numbers")
public class ClassNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int numberValue;

    @OneToMany(mappedBy = "classNumber", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public ClassNumber() {
    }

    public ClassNumber(int numberValue) {
        this.numberValue = numberValue;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
