package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.teachers;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.FullTeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers.RegisterFullTeacherService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.EmptyFieldException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users/teachers")
public class RegisterFullTeacherController {
    private final RegisterFullTeacherService registerFullTeacherService;

    @Autowired
    public RegisterFullTeacherController(RegisterFullTeacherService registerFullTeacherService) {
        this.registerFullTeacherService = registerFullTeacherService;
    }

    @PostMapping("/register-full-teacher")
    public ResponseEntity<Object> registerFullTeacher(@RequestBody FullTeacherRequest fullTeacherRequest) {
        try {
            TeacherDTO createdTeacher = registerFullTeacherService.registerUserAndTeacher(
                    fullTeacherRequest.getUserRequest(),
                    fullTeacherRequest.getTeacherRequest()
            );
            return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
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
}
