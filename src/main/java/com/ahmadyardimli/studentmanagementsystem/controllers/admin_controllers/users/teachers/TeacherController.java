package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.teachers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.exceptions.*;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.FullTeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.TeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers.FullTeacherService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final FullTeacherService fullTeacherService;

    @Autowired
    public TeacherController(TeacherService teacherService, FullTeacherService fullTeacherService) {
        this.teacherService = teacherService;
        this.fullTeacherService = fullTeacherService;
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<Object> createTeacher(@RequestBody TeacherRequest teacherRequest) {
        try {
            TeacherDTO createdTeacher = teacherService.createTeacher(teacherRequest);
            if (createdTeacher != null)
                return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Teacher registration failed.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-full-teacher/{teacherId}")
    public ResponseEntity<Object> updateFullTeacher(@PathVariable int teacherId,
                                                    @RequestBody FullTeacherRequest request) {
        try {
            TeacherDTO updated = fullTeacherService.updateUserAndTeacher(
                    teacherId,
                    request.getUserRequest(),
                    request.getTeacherRequest()
            );
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RequestValidationException | EmptyFieldException | NoChangesException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to update teacher information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-teacher/{teacherId}") // Endpoint for updating a teacher
    public ResponseEntity<Object> updateTeacher(@PathVariable int teacherId, @RequestBody TeacherRequest teacherRequest) {
        try {
            TeacherDTO updatedTeacher = teacherService.updateTeacher(teacherId, teacherRequest);

            if (updatedTeacher != null) {
                return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update teacher information.", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping // Endpoint for getting all teachers
    public ResponseEntity<Object> getAllTeachers() {
        try {
            ResponseEntity<Object> response = teacherService.getAllTeachers();
            return response;
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching teachers.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{teacherId}") // Endpoint for getting a single teacher by ID
    public ResponseEntity<Object> getTeacher(@PathVariable("teacherId") Integer teacherId) {
        try {
            TeacherDTO teacher = teacherService.getTeacherById(teacherId);

            if (teacher != null) {
                return new ResponseEntity<>(teacher, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the teacher.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("teacherId") Integer teacherId) {
        try {
            teacherService.deleteTeacherById(teacherId);
            return new ResponseEntity<>("Teacher deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the teacher.", HttpStatus.BAD_REQUEST);
        }
    }
}
