package com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "student_code", nullable = false, length = 7)
    private String studentCode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_type_id", nullable = true)
    private StudentType studentType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "sub_group_id", nullable = true)
    private SubGroup subGroup;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "foreign_language_id")
    private ForeignLanguage foreignLanguage;

    @Column(name = "father_name",  nullable = false, length = 50)
    private String fatherName;

    @Column(name = "mobile_phone", length = 50)
    private String mobilePhone;

    @Lob
    @Column(name = "school_class_code",  length = 100)
    private String schoolClassCode;

    @Lob
    @Column(name = "address",  length = 100)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_number_id", nullable = true)
    private ClassNumber classNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_letter_id", nullable = true)
    private ClassLetter classLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communication_sender_status_id", nullable = false)
    private StudentCommunicationSenderStatus studentCommunicationSenderStatus;


    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public StudentType getStudentType() {
        return studentType;
    }

    public void setStudentType(StudentType studentType) {
        this.studentType = studentType;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public SubGroup getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(SubGroup subGroup) {
        this.subGroup = subGroup;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public ForeignLanguage getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(ForeignLanguage foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSchoolClassCode() {
        return schoolClassCode;
    }

    public void setSchoolClassCode(String schoolClassCode) {
        this.schoolClassCode = schoolClassCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ClassNumber getClassNumber() {
        return classNumber;
    }
    public void setClassNumber(ClassNumber classNumber) {
        this.classNumber = classNumber;
    }

    public StudentCommunicationSenderStatus getCommunicationSenderStatus() {
        return studentCommunicationSenderStatus;
    }

    public void setCommunicationSenderStatus(StudentCommunicationSenderStatus communicationSenderUserStatus) {
        this.studentCommunicationSenderStatus = communicationSenderUserStatus;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public ClassLetter getClassLetter() {
        return classLetter;
    }
    public void setClassLetter(ClassLetter classLetter) {
        this.classLetter = classLetter;
    }
}

