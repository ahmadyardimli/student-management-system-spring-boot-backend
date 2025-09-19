package com.ahmadyardimli.studentmanagementsystem.controllers.user_controllers.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import com.ahmadyardimli.studentmanagementsystem.services.user_services.students.StudentSelfService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/students")
public class StudentSelfController {

    private final StudentSelfService selfService;

    public StudentSelfController(StudentSelfService selfService) {
        this.selfService = selfService;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        try {
            StudentDTO dto = selfService.getCurrentStudent();
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unable to fetch current student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Object> getOwn(@PathVariable Integer studentId) {
        try {
            StudentDTO dto = selfService.getOwnStudentById(studentId);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unable to fetch student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
