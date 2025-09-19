package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.StudentTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.StudentType;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.StudentTypeRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.StudentTypeService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/student-types")
public class StudentTypeController {
    private final StudentTypeService studentTypeService;

    @Autowired
    public StudentTypeController(StudentTypeService studentTypeService) {
        this.studentTypeService = studentTypeService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllStudentTypes() {
        List<StudentTypeDTO> studentTypes = studentTypeService.getAllStudentTypes();
        try {
            if (!studentTypes.isEmpty())
                return ResponseEntity.ok(studentTypes);
            else
                return new ResponseEntity<>("No student types found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching student types.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-studentType")
    public ResponseEntity<Object> createStudentType(@RequestBody StudentTypeRequest studentTypeRequest) {
        try {
            StudentTypeDTO newStudentType = studentTypeService.createStudentType(studentTypeRequest);
            if (newStudentType != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newStudentType);
            else
                return new ResponseEntity<>("An error occurred while fetching student types.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-studentType/{studentTypeId}")
    public ResponseEntity<Object> updateStudentType(
            @PathVariable Integer studentTypeId,
            @RequestBody StudentTypeRequest studentTypeRequest) {
        try {
            StudentTypeDTO updatedStudentType = studentTypeService.updateStudentType(studentTypeId, studentTypeRequest);
            if (updatedStudentType != null)
                return new ResponseEntity<>(updatedStudentType, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update student type.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{studentTypeId}")
    public ResponseEntity<Object> getStudentType(@PathVariable Integer studentTypeId) {
        try {
            StudentTypeDTO studentType = studentTypeService.getStudentTypeById(studentTypeId);
            if (studentType != null)
                return ResponseEntity.ok(studentType);
            else return new ResponseEntity<>("Student type not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the student type.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{studentTypeId}")
    public ResponseEntity<Object> deleteStudentType(@PathVariable Integer studentTypeId) {
        try {
            StudentType studentType = studentTypeService.getStudentType(studentTypeId);

            if (studentType != null){
                List<Student> students = studentType.getStudents();

                for (Student student : students)
                    student.setStudentType(null);
            }

            studentTypeService.deleteStudentTypeById(studentTypeId);
            return new ResponseEntity<>("Student type deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the student type.", HttpStatus.BAD_REQUEST);
        }
    }
}
