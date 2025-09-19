package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ClassLetterDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassLetter;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ClassLetterRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.ClassLetterService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/user_exam_details/class-letters")
public class ClassLetterController {
    private final ClassLetterService classLetterService;

    @Autowired
    public ClassLetterController(ClassLetterService classLetterService) {
        this.classLetterService = classLetterService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllClassLetters() {
        List<ClassLetterDTO> classLetters = classLetterService.getAllClassLetters();
        try {
            if (!classLetters.isEmpty()) {
                return ResponseEntity.ok(classLetters);
            } else {
                return new ResponseEntity<>("No class letters found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching class letters", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-class-letter")
    public ResponseEntity<Object> createClassLetter(@RequestBody ClassLetterRequest classLetterRequest) {
        try {
            ClassLetterDTO newClassLetter = classLetterService.createClassLetter(classLetterRequest);
            if (newClassLetter != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newClassLetter);
            } else {
                return new ResponseEntity<>("Class letter creation failed", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-class-letter/{classLetterId}")
    public ResponseEntity<Object> updateClassLetter(
            @PathVariable Integer classLetterId,
            @RequestBody ClassLetterRequest classLetterRequest) {
        try {
            ClassLetterDTO updatedClassLetter =
                    classLetterService.updateClassLetter(classLetterId, classLetterRequest);

            if (updatedClassLetter != null) {
                return new ResponseEntity<>(updatedClassLetter, HttpStatus.OK);
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

    @GetMapping("/{classLetterId}")
    public ResponseEntity<Object> getClassLetter(@PathVariable Integer classLetterId) {
        try {
            ClassLetterDTO classLetter = classLetterService.getClassLetterById(classLetterId);
            if (classLetter != null) {
                return ResponseEntity.ok(classLetter);
            } else {
                return new ResponseEntity<>("Class letter not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the class letter", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{classLetterId}")
    public ResponseEntity<Object> deleteClassLetter(@PathVariable Integer classLetterId) {
        try {
            // If wanna nullify references from Student to ClassLetter before deletion:
            ClassLetter classLetter = classLetterService.getClassLetter(classLetterId);
            if (classLetter != null && classLetter.getStudents() != null) {
                List<Student> students = classLetter.getStudents();
                for (Student student : students) {
                    student.setClassLetter(null); // Assuming 'setClassLetter' exists in Student
                }
            }

            classLetterService.deleteClassLetterById(classLetterId);
            return new ResponseEntity<>("Class letter deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the class letter", HttpStatus.BAD_REQUEST);
        }
    }
}
