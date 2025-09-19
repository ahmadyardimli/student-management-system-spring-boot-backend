package com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students;

public class StudentRequest {
    private String name;
    private String surname;
    private String studentCode;
    private int userId;
    private int studentTypeId;
    private Integer groupId;
    private Integer subGroupId;
    private int categoryId;
    private int sectionId;
    private int foreignLanguageId;
    private String fatherName;
    private String mobilePhone;
    private String schoolClassCode;
    private String address;
    private int classNumberId;
    private Integer classLetterId;
    private int communicationSenderStatusId;

    public StudentRequest() {
    }

    public StudentRequest(String name, String surname, String studentCode, int userId,
                          int studentTypeId, int groupId, int subGroupId,
                          int categoryId, int sectionId, int foreignLanguageId,
                          String fatherName, String mobilePhone, String schoolClassCode,
                          String address, int classNumberId, int classLetterId, int communicationSenderStatusId) {
        this.name = name;
        this.surname = surname;
        this.studentCode = studentCode;
        this.userId = userId;
        this.studentTypeId = studentTypeId;
        this.groupId = groupId;
        this.subGroupId = subGroupId;
        this.categoryId = categoryId;
        this.sectionId = sectionId;
        this.foreignLanguageId = foreignLanguageId;
        this.fatherName = fatherName;
        this.mobilePhone = mobilePhone;
        this.schoolClassCode = schoolClassCode;
        this.address = address;
        this.classNumberId = classNumberId;
        this.classLetterId = classLetterId;
        this.communicationSenderStatusId = communicationSenderStatusId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStudentTypeId() {
        return studentTypeId;
    }

    public void setStudentTypeId(int studentTypeId) {
        this.studentTypeId = studentTypeId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(Integer subGroupId) {
        this.subGroupId = subGroupId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getForeignLanguageId() {
        return foreignLanguageId;
    }

    public void setForeignLanguageId(int foreignLanguageId) {
        this.foreignLanguageId = foreignLanguageId;
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

    public int getCommunicationSenderStatusId() {
        return communicationSenderStatusId;
    }

    public void setCommunicationSenderStatusId(int communicationSenderStatusId) {
        this.communicationSenderStatusId = communicationSenderStatusId;
    }

    public int getClassNumberId() {
        return classNumberId;
    }

    public void setClassNumberId(int classNumberId) {
        this.classNumberId = classNumberId;
    }

    public Integer getClassLetterId() {
        return classLetterId;
    }

    public void setClassLetterId(Integer classLetterId) {
        this.classLetterId = classLetterId;
    }
}
