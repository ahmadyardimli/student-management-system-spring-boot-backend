package com.ahmadyardimli.studentmanagementsystem.services.user_services.teachers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.Teacher;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers.TeacherRepository;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers.TeacherService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TeacherSelfService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;

    public TeacherSelfService(UserRepository userRepository,
                              TeacherRepository teacherRepository,
                              TeacherService teacherService) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.teacherService = teacherService;
    }

    public TeacherDTO getCurrentTeacher() {
        // get username from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth == null) ? null : auth.getName();
        if (username == null || username.isEmpty()) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        // load User (repo returns User, not Optional)
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // find Teacher by user id
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found for user id: " + user.getId())
                );

        // return DTO via existing service API
        return teacherService.getTeacherById(teacher.getId());
    }

    public TeacherDTO getOwnTeacherById(Integer teacherId) {
        TeacherDTO dto = teacherService.getTeacherById(teacherId);
        if (dto == null) {
            throw new ResourceNotFoundException("Teacher not found.");
        }

        // Verify ownership
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth == null) ? null : auth.getName();
        if (username == null || username.isEmpty()) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // dto.getUserId() is an int in most DTOs; compare primitives
        if (dto.getUserId() != user.getId()) {
            // Hide existence to avoid leaking info
            throw new ResourceNotFoundException("Teacher not found.");
        }

        return dto;
    }
}