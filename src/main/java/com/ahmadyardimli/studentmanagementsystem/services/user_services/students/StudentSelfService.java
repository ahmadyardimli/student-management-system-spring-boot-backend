package com.ahmadyardimli.studentmanagementsystem.services.user_services.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students.StudentRepository;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students.StudentService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentSelfService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentSelfService(UserRepository userRepository,
                              StudentRepository studentRepository,
                              StudentService studentService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public StudentDTO getCurrentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth == null) ? null : auth.getName();
        if (username == null || username.isEmpty()) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found for user id: " + user.getId())
                );

        return studentService.getStudentById(student.getId());
    }

    public StudentDTO getOwnStudentById(Integer studentId) {
        StudentDTO dto = studentService.getStudentById(studentId);
        if (dto == null) {
            throw new ResourceNotFoundException("Student not found.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth == null) ? null : auth.getName();
        if (username == null || username.isEmpty()) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        if (dto.getUserId() != user.getId()) {
            // Hide existence if not the owner
            throw new ResourceNotFoundException("Student not found.");
        }

        return dto;
    }
}
