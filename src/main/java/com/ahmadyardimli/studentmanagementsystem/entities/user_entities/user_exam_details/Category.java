package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "student_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private int minClass;
    @Column(nullable = false)
    private int maxClass;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public Category(){}

    public Category(String category, int minClass, int maxClass) {
        this.category = category;
        this.minClass = minClass;
        this.maxClass = maxClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMinClass() {
        return minClass;
    }

    public void setMinClass(int minClass) {
        this.minClass = minClass;
    }

    public int getMaxClass() {
        return maxClass;
    }

    public void setMaxClass(int maxClass) {
        this.maxClass = maxClass;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}


