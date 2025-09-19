package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.commons;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<Object> registerUser(@RequestBody UserRequest userRequest) {
        try {
            UserDTO authResponse = userService.registerUser(userRequest);
            if (authResponse != null)
                return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Registration failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable int userId, @RequestBody UserRequest userRequest) {
        try {
            UserDTO updatedUser = userService.updateUser(userId, userRequest);
            if (updatedUser != null)
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            else return new ResponseEntity<>("Failed to update user information.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();

        try {
            if (!users.isEmpty())
                return ResponseEntity.ok(users);
            else
                return new ResponseEntity<>("No users found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            return new ResponseEntity<>("An error occurred while fetching users.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable("userId") Integer userId) {
        try {
            UserDTO user = userService.getUserById(userId);
            if (user != null)
                return ResponseEntity.ok(user);
            else
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Integer userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the user.", HttpStatus.BAD_REQUEST);
        }
    }
}
