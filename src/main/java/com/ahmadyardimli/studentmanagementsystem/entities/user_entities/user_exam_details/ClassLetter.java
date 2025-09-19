package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "class_letters")
public class ClassLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 5)
    private String letterValue;

    @OneToMany(mappedBy = "classLetter", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public ClassLetter() {
    }
    public ClassLetter(String letterValue) {
        this.letterValue = letterValue;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLetterValue() {
        return letterValue;
    }
    public void setLetterValue(String letterValue) {
        this.letterValue = letterValue;
    }
    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
