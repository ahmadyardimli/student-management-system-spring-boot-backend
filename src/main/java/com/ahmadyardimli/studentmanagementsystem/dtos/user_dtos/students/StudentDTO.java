package com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.*;

public class StudentDTO {
    private int id;
    private int userId;
    private String name;
    private String surname;
    private String studentCode;
    private StudentTypeDTO studentType;
    private GroupDTO group;
    private SubGroupDTO subGroup;
    private CategoryDTO category;
    private SectionDTO section;
    private ForeignLanguageDTO foreignLanguage;
    private String fatherName;
    private String mobilePhone;
    private String schoolClassCode;
    private String address;
    private ClassNumberDTO classNumber;
    private ClassLetterDTO classLetter;
    private StudentCommunicationSenderStatusDTO communicationSenderStatus;
    private UserDTO user;

    public StudentDTO() {
    }

    public StudentDTO(int id, int userId, String name, String surname, String studentCode, StudentTypeDTO studentType,
                      GroupDTO group, SubGroupDTO subGroup, CategoryDTO category, SectionDTO section,
                      ForeignLanguageDTO foreignLanguage, String fatherName, String mobilePhone,
                      String schoolClassCode, String address, ClassNumberDTO classNumber, ClassLetterDTO classLetter, StudentCommunicationSenderStatusDTO communicationSenderStatus, UserDTO user) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.studentCode = studentCode;
        this.studentType = studentType;
        this.group = group;
        this.subGroup = subGroup;
        this.category = category;
        this.section = section;
        this.foreignLanguage = foreignLanguage;
        this.fatherName = fatherName;
        this.mobilePhone = mobilePhone;
        this.schoolClassCode = schoolClassCode;
        this.address = address;
        this.classNumber = classNumber;
        this.classLetter = classLetter;
        this.communicationSenderStatus = communicationSenderStatus;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public StudentTypeDTO getStudentType() {
        return studentType;
    }

    public void setStudentType(StudentTypeDTO studentType) {
        this.studentType = studentType;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
    }

    public SubGroupDTO getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(SubGroupDTO subGroup) {
        this.subGroup = subGroup;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
    }

    public ForeignLanguageDTO getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(ForeignLanguageDTO foreignLanguage) {
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


    public StudentCommunicationSenderStatusDTO getCommunicationSenderStatus() {
        return communicationSenderStatus;
    }

    public void setCommunicationSenderStatus(StudentCommunicationSenderStatusDTO communicationSenderStatus) {
        this.communicationSenderStatus = communicationSenderStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ClassNumberDTO getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(ClassNumberDTO classNumber) {
        this.classNumber = classNumber;
    }

    public ClassLetterDTO getClassLetter() {
        return classLetter;
    }
    public void setClassLetter(ClassLetterDTO classLetter) {
        this.classLetter = classLetter;
    }
}
