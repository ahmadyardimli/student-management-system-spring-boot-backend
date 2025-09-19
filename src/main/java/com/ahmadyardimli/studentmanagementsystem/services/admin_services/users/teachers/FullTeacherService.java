package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.exceptions.EmptyFieldException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.NoChangesException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.TeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FullTeacherService {
    private final UserService userService;
    private final TeacherService teacherService;

    @Autowired
    public FullTeacherService(UserService userService,
                              TeacherService teacherService) {
        this.userService = userService;
        this.teacherService = teacherService;
    }

    @Transactional
    public TeacherDTO registerUserAndTeacher(UserRequest userRequest,
                                             TeacherRequest teacherRequest)
            throws ResourceAlreadyExistsException,
            ResourceNotFoundException,
            RequestValidationException,
            EmptyFieldException {
        // create user
        UserDTO userDTO = userService.registerUser(userRequest);

        // create teacher bound to that user
        teacherRequest.setUserId(userDTO.getId());
        return teacherService.createTeacher(teacherRequest);
    }

    @Transactional
    public TeacherDTO updateUserAndTeacher(int teacherId,
                                           UserRequest userRequest,
                                           TeacherRequest teacherRequest) {
        TeacherDTO existing = teacherService.getTeacherById(teacherId);
        int userId = existing.getUserId();

        boolean userUpdated = false;
        boolean teacherUpdated = false;

        // updating the user
        if (userRequest != null) {
            try {
                userService.updateUser(userId, userRequest);
                userUpdated = true;
            } catch (NoChangesException ex) {
                // ignore "no changes" from user
            } catch (RequestValidationException | ResourceNotFoundException
                     | ResourceAlreadyExistsException | EmptyFieldException ex) {
                throw ex;
            }
        }

        // try updating the teacher
        if (teacherRequest != null) {
            teacherRequest.setUserId(userId); // keep link
            try {
                teacherService.updateTeacher(teacherId, teacherRequest);
                teacherUpdated = true;
            } catch (NoChangesException ex) {
                // ignore "no changes" from teacher
            } catch (RequestValidationException | ResourceNotFoundException ex) {
                throw ex;
            }
        }

        if (!userUpdated && !teacherUpdated) {
            throw new NoChangesException("No changes were made.");
        }

        return teacherService.getTeacherById(teacherId);
    }
}
