package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.exceptions.*;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students.StudentRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FullStudentService {
    private final UserService userService;
    private final StudentService studentService;

    @Autowired
    public FullStudentService(UserService userService,
                              StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }

    @Transactional
    public StudentDTO registerUserAndStudent(UserRequest userRequest,
                                             StudentRequest studentRequest)
            throws ResourceAlreadyExistsException,
            ResourceNotFoundException,
            RequestValidationException,
            EmptyFieldException {
        // Create the user
        UserDTO userDTO = userService.registerUser(userRequest);

        // Create the student under that user
        studentRequest.setUserId(userDTO.getId());
        return studentService.createStudent(studentRequest);
    }

    @Transactional
    public StudentDTO updateUserAndStudent(int studentId,
                                           UserRequest userRequest,
                                           StudentRequest studentRequest) {
        StudentDTO existing = studentService.getStudentById(studentId);
        int userId = existing.getUserId();

        boolean userUpdated = false;
        boolean studentUpdated = false;

        if (userRequest != null) {
            try {
                userService.updateUser(userId, userRequest);
                userUpdated = true;
            } catch (NoChangesException ex) {
              // ignore
            } catch (RequestValidationException | ResourceNotFoundException
                     | ResourceAlreadyExistsException
                     | EmptyFieldException ex) {
                throw ex;
            }
        }

        // Try updating the student
        if (studentRequest != null) {
            studentRequest.setUserId(userId);
            try {
                studentService.updateStudent(studentId, studentRequest);
                studentUpdated = true;
            }
            catch (NoChangesException ex){
                // ignore
            }
            catch (RequestValidationException | ResourceNotFoundException | ResourceAlreadyExistsException
                     | EmptyFieldException ex) {
                throw ex;
            }
        }

        if (!userUpdated && !studentUpdated) {
            throw new NoChangesException("No changes were made.");
        }

        return studentService.getStudentById(studentId);
    }
}
