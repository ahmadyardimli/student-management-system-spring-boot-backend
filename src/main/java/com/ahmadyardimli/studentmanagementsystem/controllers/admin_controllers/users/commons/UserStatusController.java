package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.commons;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/users/user-statuses")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @Autowired
    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserStatuses() {
        List<UserStatusDTO> userStatuses = userStatusService.getAllStatuses();
        try {
            if (!userStatuses.isEmpty())
                return ResponseEntity.ok(userStatuses);
            else
                return new ResponseEntity<>("No user statuses found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching user statuses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
