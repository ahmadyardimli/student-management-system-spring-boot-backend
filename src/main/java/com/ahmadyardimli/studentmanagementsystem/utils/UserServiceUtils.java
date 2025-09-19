package com.ahmadyardimli.studentmanagementsystem.utils;

import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students.StudentRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceUtils {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserServiceUtils(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public void checkIfUserIdExistsInStudentOrTeacherOrThrow(Integer userId, String entityName) {
        boolean userIdExistsInStudent = studentRepository.existsByUser_Id(userId);
        boolean userIdExistsInTeacher = teacherRepository.existsByUser_Id(userId);

        if (userIdExistsInStudent || userIdExistsInTeacher) {
            throw new ResourceAlreadyExistsException("User with user ID " + userId + " already exists as a " + entityName);
        }
    }
}
