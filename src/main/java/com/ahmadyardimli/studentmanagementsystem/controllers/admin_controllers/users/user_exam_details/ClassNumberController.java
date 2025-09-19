package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ClassNumberDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassNumber;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ClassNumberRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.ClassNumberService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/class-numbers")
public class ClassNumberController {
    private final ClassNumberService classNumberService;
    @Autowired
    public ClassNumberController(ClassNumberService classNumberService) {
        this.classNumberService = classNumberService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllClassNumbers() {
        List<ClassNumberDTO> classNumbers = classNumberService.getAllClassNumbers();
        try {
            if (!classNumbers.isEmpty()) {
                return ResponseEntity.ok(classNumbers);
            } else {
                return new ResponseEntity<>("No class numbers found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching class numbers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-class-number")
    public ResponseEntity<Object> createClassNumber(@RequestBody ClassNumberRequest classNumberRequest) {
        try {
            ClassNumberDTO newClassNumber = classNumberService.createClassNumber(classNumberRequest);
            if (newClassNumber != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newClassNumber);
            } else {
                return new ResponseEntity<>("Class number creation failed", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-class-number/{classNumberId}")
    public ResponseEntity<Object> updateClassNumber(
            @PathVariable Integer classNumberId,
            @RequestBody ClassNumberRequest classNumberRequest) {
        try {
            ClassNumberDTO updatedClassNumber =
                    classNumberService.updateClassNumber(classNumberId, classNumberRequest);

            if (updatedClassNumber != null) {
                return new ResponseEntity<>(updatedClassNumber, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Update failed", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{classNumberId}")
    public ResponseEntity<Object> getClassNumber(@PathVariable Integer classNumberId) {
        try {
            ClassNumberDTO classNumber = classNumberService.getClassNumberById(classNumberId);
            if (classNumber != null) {
                return ResponseEntity.ok(classNumber);
            } else {
                return new ResponseEntity<>("Class number not found", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the class number", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{classNumberId}")
    public ResponseEntity<Object> deleteClassNumber(@PathVariable Integer classNumberId) {
        try {
            // If decide to nullify references from Student to ClassNumber before deletion,
            ClassNumber classNumber = classNumberService.getClassNumber(classNumberId);
            if (classNumber != null && classNumber.getStudents() != null) {
                List<Student> students = classNumber.getStudents();
                for (Student student : students) {
                    student.setClassNumber(null); // I assume 'setClassNumber' exists in Student
                }
            }

            classNumberService.deleteClassNumberById(classNumberId);
            return new ResponseEntity<>("Class number deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the class number", HttpStatus.BAD_REQUEST);
        }
    }
}
