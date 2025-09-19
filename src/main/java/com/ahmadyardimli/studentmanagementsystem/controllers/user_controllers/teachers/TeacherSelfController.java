package com.ahmadyardimli.studentmanagementsystem.controllers.user_controllers.teachers;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.services.user_services.teachers.TeacherSelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/teachers")
public class TeacherSelfController {

    private final TeacherSelfService selfService;

    public TeacherSelfController(TeacherSelfService selfService) {
        this.selfService = selfService;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me() {
        try {
            TeacherDTO dto = selfService.getCurrentTeacher();
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unable to fetch current teacher.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<Object> getOwn(@PathVariable Integer teacherId) {
        try {
            TeacherDTO dto = selfService.getOwnTeacherById(teacherId);
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unable to fetch teacher.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
