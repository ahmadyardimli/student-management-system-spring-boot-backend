package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "foreign_languages")
public class ForeignLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String foreignLanguage;

    @OneToMany(mappedBy = "foreignLanguage", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(String foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}

