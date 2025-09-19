package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ForeignLanguageDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ForeignLanguage;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ForeignLanguageRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.ForeignLanguageService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/foreign-languages")
public class ForeignLanguageController {
    private final ForeignLanguageService foreignLanguageService;

    @Autowired
    public ForeignLanguageController(ForeignLanguageService foreignLanguageService) {
        this.foreignLanguageService = foreignLanguageService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllForeignLanguages() {
        try {
            List<ForeignLanguageDTO> foreignLanguages = foreignLanguageService.getAllForeignLanguages();
            if (!foreignLanguages.isEmpty()) {
                return ResponseEntity.ok(foreignLanguages);
            } else {
                return new ResponseEntity<>("No foreign languages found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching foreign languages.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-foreign-language")
    public ResponseEntity<Object> createForeignLanguage(@RequestBody ForeignLanguageRequest foreignLanguageRequest) {
        try {
            ForeignLanguageDTO newForeignLanguage = foreignLanguageService.createForeignLanguage(foreignLanguageRequest);
            if (newForeignLanguage != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newForeignLanguage);
            } else {
                return new ResponseEntity<>("Failed to create foreign language.", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-foreign-language/{foreignLanguageId}")
    public ResponseEntity<Object> updateForeignLanguage(
            @PathVariable Integer foreignLanguageId,
            @RequestBody ForeignLanguageRequest foreignLanguageRequest) {
        try {
            ForeignLanguageDTO updatedForeignLanguage = foreignLanguageService.updateForeignLanguage(foreignLanguageId, foreignLanguageRequest);
            if (updatedForeignLanguage != null) {
                return new ResponseEntity<>(updatedForeignLanguage, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Foreign language not found.", HttpStatus.NOT_FOUND);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{foreignLanguageId}")
    public ResponseEntity<Object> getForeignLanguage(@PathVariable Integer foreignLanguageId) {
        try {
            ForeignLanguageDTO foreignLanguage = foreignLanguageService.getForeignLanguageById(foreignLanguageId);
            if (foreignLanguage != null) {
                return ResponseEntity.ok(foreignLanguage);
            } else {
                return new ResponseEntity<>("Foreign language not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the foreign language.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{foreignLanguageId}")
    public ResponseEntity<Object> deleteForeignLanguage(@PathVariable Integer foreignLanguageId) {
        try {
            ForeignLanguage foreignLanguage = foreignLanguageService.getForeignLanguage(foreignLanguageId);

            if (foreignLanguage != null) {
                List<Student> students = foreignLanguage.getStudents();

                for (Student student : students)
                    student.setForeignLanguage(null);
            }

            foreignLanguageService.deleteForeignLanguageById(foreignLanguageId);
            return new ResponseEntity<>("Foreign language deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the foreign language.", HttpStatus.BAD_REQUEST);
        }
    }
}