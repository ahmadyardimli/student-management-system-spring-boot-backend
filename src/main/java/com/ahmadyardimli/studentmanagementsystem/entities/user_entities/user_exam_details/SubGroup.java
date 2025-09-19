package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sub_groups")
public class SubGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sub_group", nullable = false, length = 30)
    private String subGroup;

    @OneToMany(mappedBy = "subGroup", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Student> students;

    public SubGroup() {}
    public SubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}


