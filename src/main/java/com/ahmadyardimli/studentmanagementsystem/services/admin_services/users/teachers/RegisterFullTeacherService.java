package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.TeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterFullTeacherService {
    private final UserService userService;
    private final TeacherService teacherService;

    @Autowired
    public RegisterFullTeacherService(UserService userService, TeacherService teacherService) {
        this.userService = userService;
        this.teacherService = teacherService;
    }

    // both user and teacher creation happen together
    @Transactional
    public TeacherDTO registerUserAndTeacher(UserRequest userRequest, TeacherRequest teacherRequest) {
        // register User
        UserDTO userDTO = userService.registerUser(userRequest);

        //register Teacher using the created userId
        teacherRequest.setUserId(userDTO.getId());
        TeacherDTO teacherDTO = teacherService.createTeacher(teacherRequest);

        // both user and teacher creation successful
        return teacherDTO;
    }
}
