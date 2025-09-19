package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String section;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public Section() {
    }

    public Section(String section) {
        this.section = section;
    }
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}


