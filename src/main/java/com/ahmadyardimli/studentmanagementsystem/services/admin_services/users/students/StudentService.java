package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.*;
import com.ahmadyardimli.studentmanagementsystem.exceptions.*;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.*;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.StudentCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.student_mappers.StudentDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students.StudentRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students.StudentRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.StudentTypeService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.SubGroupService;
import com.ahmadyardimli.studentmanagementsystem.utils.UserServiceUtils;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentDTOMapper studentDTOMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final StudentCommunicationSenderStatusService studentCommunicationSenderStatusService;
    private final StudentTypeService studentTypeService;
    private final GroupService groupService;
    private final SubGroupService subGroupService;
    private final ClassNumberService classNumberService;
    private final CategoryService categoryService;
    private final SectionService sectionService;
    private final ForeignLanguageService foreignLanguageService;
    private final UserServiceUtils userServiceUtils;
    private final ClassLetterService classLetterService;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          StudentDTOMapper studentDTOMapper,
                          UserRepository userRepository,
                          UserService userService,
                          StudentCommunicationSenderStatusService studentCommunicationSenderStatusService,
                          StudentTypeService studentTypeService,
                          GroupService groupService,
                          SubGroupService subGroupService,
                          ClassNumberService classNumberService,
                          CategoryService categoryService,
                          SectionService sectionService,
                          ForeignLanguageService foreignLanguageService,
                          UserServiceUtils userServiceUtils,
                          ClassLetterService classLetterService) {
        this.studentRepository = studentRepository;
        this.studentDTOMapper = studentDTOMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.studentCommunicationSenderStatusService = studentCommunicationSenderStatusService;
        this.studentTypeService = studentTypeService;
        this.groupService = groupService;
        this.subGroupService = subGroupService;
        this.classNumberService = classNumberService;
        this.categoryService = categoryService;
        this.sectionService = sectionService;
        this.foreignLanguageService = foreignLanguageService;
        this.userServiceUtils = userServiceUtils;
        this.classLetterService = classLetterService;
    }

    public StudentDTO createStudent(StudentRequest studentRequest) {
        int userId = studentRequest.getUserId();
        userServiceUtils.checkIfUserIdExistsInStudentOrTeacherOrThrow(userId, "student");
        validateStudentRequest(studentRequest, false, null);

        User user = userService.getUser(userId);
        StudentCommunicationSenderStatus communicationSenderStatus =
                studentCommunicationSenderStatusService
                        .getStudentCommunicationSenderStatus(studentRequest.getCommunicationSenderStatusId());
        StudentType studentType = studentTypeService.getStudentType(studentRequest.getStudentTypeId());
        Category category = categoryService.getStudentCategory(studentRequest.getCategoryId());
        Section section = sectionService.getSection(studentRequest.getSectionId());
        ForeignLanguage foreignLanguage = foreignLanguageService.getForeignLanguage(studentRequest.getForeignLanguageId());
        ClassNumber classNumber = classNumberService.getClassNumber(studentRequest.getClassNumberId());

        Group group = null;
        if (studentRequest.getGroupId() != null) {
            group = groupService.getGroup(studentRequest.getGroupId());
        }

        SubGroup subGroup = null;
        if (studentRequest.getSubGroupId() != null) {
            subGroup = subGroupService.getSubGroup(studentRequest.getSubGroupId());
        }

        ClassLetter classLetter = null;
        if (studentRequest.getClassLetterId() != null && studentRequest.getClassLetterId() != 0) {
            classLetter = classLetterService.getClassLetter(studentRequest.getClassLetterId());
        }

        String mobilePhone = null;
        if (!isNullOrEmpty(studentRequest.getMobilePhone())) {
            mobilePhone = studentRequest.getMobilePhone().trim();
        }

        String schoolClassCode = null;
        if (!isNullOrEmpty(studentRequest.getSchoolClassCode())) {
            schoolClassCode = studentRequest.getSchoolClassCode().trim();
        }

        String address = null;
        if (!isNullOrEmpty(studentRequest.getAddress())) {
            address = studentRequest.getAddress().trim();
        }

        Student student = new Student();
        student.setName(studentRequest.getName());
        student.setSurname(studentRequest.getSurname());
        student.setStudentCode(studentRequest.getStudentCode());
        student.setFatherName(studentRequest.getFatherName());
        student.setMobilePhone(mobilePhone);
        student.setSchoolClassCode(schoolClassCode);
        student.setAddress(address);
        student.setClassNumber(classNumber);
        student.setUser(user);
        student.setCommunicationSenderStatus(communicationSenderStatus);
        student.setStudentType(studentType);
        student.setGroup(group);
        student.setSubGroup(subGroup);
        student.setClassLetter(classLetter);
        student.setCategory(category);
        student.setSection(section);
        student.setForeignLanguage(foreignLanguage);

        Student savedStudent = studentRepository.save(student);
        return studentDTOMapper.apply(savedStudent);
    }

    public List<StudentDTO> filterStudents(Integer studentTypeId,
                                           Integer categoryId,
                                           Integer foreignLanguageId,
                                           Integer bolmeId,
                                           Integer groupId,
                                           Integer altGroupId,
                                           Integer communicationStatusId,
                                           Integer classNumberId,
                                           Integer classLetterId,
                                           Integer userStatusId,
                                           Integer userTypeId) {
        List<Student> matched = studentRepository.filterStudents(
                studentTypeId, categoryId, foreignLanguageId, bolmeId,
                groupId, altGroupId, communicationStatusId,
                classNumberId, classLetterId, userStatusId, userTypeId);

        return matched.stream()
                .map(studentDTOMapper)
                .collect(Collectors.toList());
    }

    public StudentDTO updateStudent(int studentId, StudentRequest studentRequest) {
        validateStudentRequest(studentRequest, true, studentId);

        Student existing = getStudent(studentId);
        boolean changes = false;

        if (studentRequest.getName() != null &&
                !studentRequest.getName().equals(existing.getName())) {
            existing.setName(studentRequest.getName());
            changes = true;
        }

        if (studentRequest.getSurname() != null &&
                !studentRequest.getSurname().equals(existing.getSurname())) {
            existing.setSurname(studentRequest.getSurname());
            changes = true;
        }

        if (studentRequest.getStudentCode() != null &&
                !studentRequest.getStudentCode().equals(existing.getStudentCode())) {
            existing.setStudentCode(studentRequest.getStudentCode());
            changes = true;
        }

        if (studentRequest.getFatherName() != null &&
                !studentRequest.getFatherName().equals(existing.getFatherName())) {
            existing.setFatherName(studentRequest.getFatherName());
            changes = true;
        }

        if (studentRequest.getMobilePhone() != null) {
            String incoming = studentRequest.getMobilePhone().trim();
            if (incoming.isEmpty()) {
                if (existing.getMobilePhone() != null) {
                    existing.setMobilePhone(null);
                    changes = true;
                }
            } else {
                if (!incoming.equals(existing.getMobilePhone())) {
                    existing.setMobilePhone(incoming);
                    changes = true;
                }
            }
        }

        if (studentRequest.getSchoolClassCode() != null) {
            String in = studentRequest.getSchoolClassCode().trim();
            if (in.isEmpty()) {
                if (existing.getSchoolClassCode() != null) {
                    existing.setSchoolClassCode(null);
                    changes = true;
                }
            } else if (!in.equals(existing.getSchoolClassCode())) {
                existing.setSchoolClassCode(in);
                changes = true;
            }
        }

        if (studentRequest.getAddress() != null) {
            String in = studentRequest.getAddress().trim();
            if (in.isEmpty()) {
                if (existing.getAddress() != null) {
                    existing.setAddress(null);
                    changes = true;
                }
            } else if (!in.equals(existing.getAddress())) {
                existing.setAddress(in);
                changes = true;
            }
        }

        if (studentRequest.getUserId() != 0 &&
                studentRequest.getUserId() != existing.getUser().getId()) {
            userServiceUtils.checkIfUserIdExistsInStudentOrTeacherOrThrow(
                    studentRequest.getUserId(), "student");
            User user = userService.getUser(studentRequest.getUserId());
            existing.setUser(user);
            changes = true;
        }

        if (studentRequest.getCommunicationSenderStatusId() != 0) {
            boolean shouldUpdate = existing.getCommunicationSenderStatus() == null ||
                    studentRequest.getCommunicationSenderStatusId() != existing.getCommunicationSenderStatus().getId();
            if (shouldUpdate) {
                StudentCommunicationSenderStatus scs =
                        studentCommunicationSenderStatusService
                                .getStudentCommunicationSenderStatus(studentRequest.getCommunicationSenderStatusId());
                existing.setCommunicationSenderStatus(scs);
                changes = true;
            }
        }

        if (studentRequest.getStudentTypeId() != 0) {
            boolean shouldUpdate = existing.getStudentType() == null ||
                    studentRequest.getStudentTypeId() != existing.getStudentType().getId();
            if (shouldUpdate) {
                StudentType st = studentTypeService.getStudentType(studentRequest.getStudentTypeId());
                existing.setStudentType(st);
                changes = true;
            }
        }

        // group
        if (studentRequest.getGroupId() != null) {
            if (studentRequest.getGroupId() == 0) {
                // client explicitly chose “empty” - clear
                if (existing.getGroup() != null) {
                    existing.setGroup(null);
                    changes = true;
                }
            } else {
                // client chose a real group - update if different
                Integer newGroupId = studentRequest.getGroupId();
                if (existing.getGroup() == null ||
                        !newGroupId.equals(existing.getGroup().getId())) {
                    Group g = groupService.getGroup(newGroupId);
                    existing.setGroup(g);
                    changes = true;
                }
            }
        }

        // sub group
        if (studentRequest.getSubGroupId() != null) {
            if (studentRequest.getSubGroupId() == 0) {
                if (existing.getSubGroup() != null) {
                    existing.setSubGroup(null);
                    changes = true;
                }
            } else {
                Integer newAltGroupId = studentRequest.getSubGroupId();
                if (existing.getSubGroup() == null ||
                        !newAltGroupId.equals(existing.getSubGroup().getId())) {
                    SubGroup ag = subGroupService.getSubGroup(newAltGroupId);
                    existing.setSubGroup(ag);
                    changes = true;
                }
            }
        }

        if (studentRequest.getCategoryId() != 0) {
            boolean shouldUpdate = existing.getCategory() == null ||
                    studentRequest.getCategoryId() != existing.getCategory().getId();
            if (shouldUpdate) {
                Category cat = categoryService.getStudentCategory(studentRequest.getCategoryId());
                existing.setCategory(cat);
                changes = true;
            }
        }

        if (studentRequest.getSectionId() != 0) {
            boolean shouldUpdate = existing.getSection() == null ||
                    studentRequest.getSectionId() != existing.getSection().getId();
            if (shouldUpdate) {
                Section sb = sectionService.getSection(studentRequest.getSectionId());
                existing.setSection(sb);
                changes = true;
            }
        }

        if (studentRequest.getForeignLanguageId() != 0) {
            boolean shouldUpdate = existing.getForeignLanguage() == null ||
                    studentRequest.getForeignLanguageId() != existing.getForeignLanguage().getId();
            if (shouldUpdate) {
                ForeignLanguage fl =
                        foreignLanguageService.getForeignLanguage(studentRequest.getForeignLanguageId());
                existing.setForeignLanguage(fl);
                changes = true;
            }
        }

        // class number
        if (studentRequest.getClassNumberId() != 0) {
            boolean shouldUpdate = existing.getClassNumber() == null
                    || studentRequest.getClassNumberId() != existing.getClassNumber().getId();
            if (shouldUpdate) {
                ClassNumber cn = classNumberService.getClassNumber(studentRequest.getClassNumberId());
                existing.setClassNumber(cn);
                changes = true;
            }
        }

        // class letter
        Integer newClassLetterId = studentRequest.getClassLetterId();
        if (newClassLetterId != null) {
            if (newClassLetterId == 0) {
                if (existing.getClassLetter() != null) {
                    existing.setClassLetter(null);
                    changes = true;
                }
            } else {
                if (existing.getClassLetter() == null
                        || !newClassLetterId.equals(existing.getClassLetter().getId())) {
                    ClassLetter cl = classLetterService.getClassLetter(newClassLetterId);
                    existing.setClassLetter(cl);
                    changes = true;
                }
            }
        }

        if (!changes) {
            throw new NoChangesException("No changes were made.");
        }

        User u = existing.getUser();
        u.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u);

        Student saved = studentRepository.save(existing);
        return studentDTOMapper.apply(saved);
    }

    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentDTOMapper)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(int studentId) {
        checkIfStudentExistsOrThrow(studentId);
        return studentRepository.findById(studentId)
                .map(studentDTOMapper)
                .orElse(null);
    }

    public Student getStudent(int studentId) {
        checkIfStudentExistsOrThrow(studentId);
        return studentRepository.findById(studentId)
                .orElse(null);
    }

    public void deleteStudentById(int studentId) {
        checkIfStudentExistsOrThrow(studentId);
        studentRepository.deleteById(studentId);
    }

    private void checkIfStudentExistsOrThrow(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found");
        }
    }

    private void validateStudentRequest(StudentRequest studentRequest,
                                        boolean isUpdate,
                                        Integer updatingId) {
        int classNumberId = studentRequest.getClassNumberId();
        if (isUpdate) {
            Student existing = getStudent(updatingId);
            if (classNumberId <= 0 && existing.getClassNumber() != null) {
                classNumberId = existing.getClassNumber().getId();
            }
        }

        Integer classLetterId = studentRequest.getClassLetterId();
        if (classLetterId != null && classLetterId != 0 && classNumberId == 0) {
            throw new RequestValidationException(
                    "Class letter cannot be set without selecting a class number."
            );
        }

        // Name
        if (!isUpdate || studentRequest.getName() != null) {
            if (isNullOrEmpty(studentRequest.getName())) {
                if (!isUpdate) throw new EmptyFieldException("Name is required.");
            } else {
                ValidationUtils.validateNameOrSurname(studentRequest.getName(), "Name");
            }
        }

        // Surname
        if (!isUpdate || studentRequest.getSurname() != null) {
            if (isNullOrEmpty(studentRequest.getSurname())) {
                if (!isUpdate) throw new EmptyFieldException("Surname is required.");
            } else {
                ValidationUtils.validateNameOrSurname(studentRequest.getSurname(), "Surname");
            }
        }

        // FatherName
        if (!isUpdate || studentRequest.getFatherName() != null) {
            if (isNullOrEmpty(studentRequest.getFatherName())) {
                if (!isUpdate) throw new EmptyFieldException("Father's name is required.");
            } else {
                ValidationUtils.validateNameOrSurname(studentRequest.getFatherName(), "Father's name");
            }
        }

        // StudentCode
        if (!isUpdate || studentRequest.getStudentCode() != null) {
            if (isNullOrEmpty(studentRequest.getStudentCode())) {
                if (!isUpdate) throw new EmptyFieldException("Student code is required.");
            } else {
                ValidationUtils.validateStudentCode(studentRequest.getStudentCode());
                boolean exists = updatingId == null
                        ? studentRepository.existsByStudentCode(studentRequest.getStudentCode())
                        : studentRepository.existsByStudentCodeAndIdNot(
                        studentRequest.getStudentCode(), updatingId);
                if (exists) {
                    throw new ResourceAlreadyExistsException(
                            "Student code " + studentRequest.getStudentCode() + " already exists.");
                }
            }
        }

        // MobilePhone (optional)
        if (studentRequest.getMobilePhone() != null &&
                !studentRequest.getMobilePhone().trim().isEmpty()) {
            ValidationUtils.validateMobilePhone(studentRequest.getMobilePhone());
        }

        // ClassNumber required on create
        if (!isUpdate && studentRequest.getClassNumberId() == -1) {
            throw new EmptyFieldException("Class number is required.");
        }

        // category + class number check for both CREATE & UPDATE
        int categoryId = studentRequest.getCategoryId();

        if (isUpdate) {
            // fall back to existing values when the client didn't send a new one
            Student existing = getStudent(updatingId);
            if (categoryId == 0 && existing.getCategory()    != null) {
                categoryId = existing.getCategory().getId();
            }
            if (classNumberId <= 0 && existing.getClassNumber() != null) {
                classNumberId = existing.getClassNumber().getId();
            }
        }

        if (categoryId != 0 && classNumberId > 0) {
            ClassNumber     cn  = classNumberService.getClassNumber(classNumberId);
            Category cat = categoryService.getStudentCategory(categoryId);
            int num = cn.getNumberValue(), min = cat.getMinClass(), max = cat.getMaxClass();
            if (num < min || num > max) {
                throw new RequestValidationException(
                        "The student's class number is not within the selected category's class range. " +
                                "Category: " + cat.getCategory() +
                                ", Class range: " + min + "-" + max +
                                ", Entered class: " + num
                );
            }
        }
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
