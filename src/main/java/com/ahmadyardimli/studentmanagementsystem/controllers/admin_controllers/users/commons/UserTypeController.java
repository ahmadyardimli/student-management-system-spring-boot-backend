package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.commons;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserTypeRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserTypeService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/users/user-types")
public class UserTypeController {
    private final UserTypeService userTypeService;

    @Autowired
    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserTypes() {
        List<UserTypeDTO> userTypes = userTypeService.getAllUserTypes();
        try {
            if (!userTypes.isEmpty())
                return ResponseEntity.ok(userTypes);
            else
                return new ResponseEntity<>("No user types found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching user types.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-userType")
    public ResponseEntity<Object> createUserType(@RequestBody UserTypeRequest userTypeRequest) {
        try {
            UserTypeDTO newUserType = userTypeService.saveUserType(userTypeRequest);
            if (newUserType != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newUserType);
            else
                return new ResponseEntity<>("Failed to create user type.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-userType/{userTypeId}")
    public ResponseEntity<Object> updateUserType(
            @PathVariable Integer userTypeId,
            @RequestBody UserTypeRequest userTypeRequest) {
        try {
            UserTypeDTO updatedUserType = userTypeService.updateUserType(userTypeId, userTypeRequest);
            if (updatedUserType != null)
                return new ResponseEntity<>(updatedUserType, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update user type.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{userTypeId}")
    public ResponseEntity<Object> getUserType(@PathVariable Integer userTypeId) {
        try {
            UserTypeDTO userType = userTypeService.getUserTypeById(userTypeId);
            if (userType != null)
                return ResponseEntity.ok(userType);
            else return new ResponseEntity<>("User type not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the user type.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userTypeId}")
    public ResponseEntity<Object> deleteUserType(@PathVariable int userTypeId) {
        try {
            userTypeService.deleteUserTypeById(userTypeId);
            return ResponseEntity.ok("User type deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RequestValidationException ex) {
            // Return a 400 Bad Request with the exception message
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the user type.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
