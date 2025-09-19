package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SubjectDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SubjectRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.SubjectService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        try {
            if (!subjects.isEmpty())
                return ResponseEntity.ok(subjects);
            else
                return new ResponseEntity<>("No subjects found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching subjects.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-subject")
    public ResponseEntity<Object> createSubject(@RequestBody SubjectRequest subjectRequest) {
        try {
            SubjectDTO newSubject = subjectService.createSubject(subjectRequest);
            if (newSubject != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newSubject);
            else
                return new ResponseEntity<>("Failed to create subject.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-subject/{subjectId}")
    public ResponseEntity<Object> updateSubject(
            @PathVariable Integer subjectId,
            @RequestBody SubjectRequest subjectRequest) {
        try {
            SubjectDTO updatedSubject = subjectService.updateSubject(subjectId, subjectRequest);
            if (updatedSubject != null)
                return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update subject.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<Object> getSubject(@PathVariable Integer subjectId) {
        try {
            SubjectDTO subject = subjectService.getSubjectById(subjectId);
            if (subject != null)
                return ResponseEntity.ok(subject);
            else return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the subject.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Object> deleteSubject(@PathVariable Integer subjectId) {
        try {
            subjectService.deleteSubjectById(subjectId);
            return new ResponseEntity<>("Subject deleted successfully.", HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the subject.", HttpStatus.BAD_REQUEST);
        }
    }
}
