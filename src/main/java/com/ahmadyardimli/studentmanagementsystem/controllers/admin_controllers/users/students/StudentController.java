package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import com.ahmadyardimli.studentmanagementsystem.exceptions.*;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students.FullStudentRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students.StudentRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students.FullStudentService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/users/students")
public class StudentController {
    private final StudentService studentService;
    private final FullStudentService fullStudentService;

    @Autowired
    public StudentController(StudentService studentService,
                             FullStudentService fullStudentService) {
        this.studentService = studentService;
        this.fullStudentService = fullStudentService;
    }

    @PostMapping("/create-student")
    public ResponseEntity<Object> createStudent(@RequestBody StudentRequest studentRequest) {
        try {
            StudentDTO createdStudent = studentService.createStudent(studentRequest);
            if (createdStudent != null) {
                return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create student.", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while creating the student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register-full-student")
    public ResponseEntity<Object> registerFullStudent(@RequestBody FullStudentRequest registerFullStudentRequest) {
        try {
            StudentDTO createdStudent = fullStudentService.registerUserAndStudent(
                    registerFullStudentRequest.getUserRequest(),
                    registerFullStudentRequest.getStudentRequest()
            );
            return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EmptyFieldException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update-full-student/{studentId}")
    public ResponseEntity<Object> updateFullStudent(@PathVariable int studentId,
                                                    @RequestBody FullStudentRequest fullStudentRequest) {
        try {
            StudentDTO updatedStudent = fullStudentService.updateUserAndStudent(
                    studentId,
                    fullStudentRequest.getUserRequest(),
                    fullStudentRequest.getStudentRequest()
            );
            if (updatedStudent != null) {
                return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update student information.", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (EmptyFieldException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoChangesException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while updating student information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterStudents(
            @RequestParam(value = "studentTypeId", required = false) Integer studentTypeId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "foreignLanguageId", required = false) Integer foreignLanguageId,
            @RequestParam(value = "sectionId", required = false) Integer sectionId,
            @RequestParam(value = "groupId", required = false) Integer groupId,
            @RequestParam(value = "subGroupId", required = false) Integer subGroupId,
            @RequestParam(value = "communicationStatusId", required = false) Integer communicationStatusId,
            @RequestParam(value = "classNumberId", required = false) Integer classNumberId,
            @RequestParam(value = "classLetterId", required = false) Integer classLetterId,
            @RequestParam(value = "userStatusId", required = false) Integer userStatusId,
            @RequestParam(value = "userTypeId", required = false) Integer userTypeId
    ) {
        try {
            List<StudentDTO> filteredStudents = studentService.filterStudents(
                    studentTypeId,
                    categoryId,
                    foreignLanguageId,
                    sectionId,
                    groupId,
                    subGroupId,
                    communicationStatusId,
                    classNumberId,
                    classLetterId,
                    userStatusId,
                    userTypeId
            );

            if (!filteredStudents.isEmpty()) {
                return new ResponseEntity<>(filteredStudents, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No students found for the selected criteria.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while filtering students.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-student/{studentId}")
    public ResponseEntity<Object> updateStudent(@PathVariable int studentId, @RequestBody StudentRequest studentRequest) {
        try {
            StudentDTO updatedStudent = studentService.updateStudent(studentId, studentRequest);

            if (updatedStudent != null) {
                return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update student information.", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while updating student information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        try {
            if (!students.isEmpty()) {
                return ResponseEntity.ok(students);
            } else {
                return new ResponseEntity<>("No students found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching students.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Object> getStudent(@PathVariable("studentId") Integer studentId) {
        try {
            StudentDTO student = studentService.getStudentById(studentId);

            if (student != null) {
                return new ResponseEntity<>(student, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the student.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("studentId") Integer studentId) {
        try {
            studentService.deleteStudentById(studentId);
            return new ResponseEntity<>("Student deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the student.", HttpStatus.BAD_REQUEST);
        }
    }
}