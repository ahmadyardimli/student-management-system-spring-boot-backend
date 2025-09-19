package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SectionDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Section;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SectionRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.SectionService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/sections")
public class SectionController {
    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSections() {
        try {
            List<SectionDTO> sections = sectionService.getAllSections();
            if (!sections.isEmpty()) {
                return ResponseEntity.ok(sections);
            } else {
                return new ResponseEntity<>("No sections found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching sections.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-section")
    public ResponseEntity<Object> createSection(@RequestBody SectionRequest sectionRequest) {
        try {
            SectionDTO newSection = sectionService.createSection(sectionRequest);
            if (newSection != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newSection);
            } else {
                return new ResponseEntity<>("Failed to create section.", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-section/{sectionId}")
    public ResponseEntity<Object> updateSection(
            @PathVariable Integer sectionId,
            @RequestBody SectionRequest sectionRequest) {
        try {
            SectionDTO updatedSection = sectionService.updateSection(sectionId, sectionRequest);
            if (updatedSection != null) {
                return new ResponseEntity<>(updatedSection, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update section.", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<Object> getSection(@PathVariable Integer sectionId) {
        try {
            SectionDTO section = sectionService.getSectionById(sectionId);
            if (section != null) {
                return ResponseEntity.ok(section);
            } else {
                return new ResponseEntity<>("Section not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the section.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Object> deleteSection(@PathVariable Integer sectionId) {
        try {
            Section section = sectionService.getSection(sectionId);
            if (section != null){
                List<Student> students = section.getStudents();
                for (Student student : students) {
                    student.setSection(null);
                }
            }

            sectionService.deleteSectionById(sectionId);
            return new ResponseEntity<>("Section deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the section.", HttpStatus.BAD_REQUEST);
        }
    }
}