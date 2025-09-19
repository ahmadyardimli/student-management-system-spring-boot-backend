package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.admins;

import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminStatusRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins.AdminStatusService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/statuses")
public class AdminStatusController {
    private final AdminStatusService adminStatusService;

    @Autowired
    public AdminStatusController(AdminStatusService adminStatusService) {
        this.adminStatusService = adminStatusService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllStatuses() {
        try {
            List<AdminStatusDTO> statuses = adminStatusService.getAllStatuses();
            if (statuses.isEmpty()) {
                return new ResponseEntity<>("No admin statuses found.", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(statuses);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching admin statuses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{adminStatusId}")
    public ResponseEntity<Object> getStatusById(@PathVariable Integer adminStatusId) {
        try {
            AdminStatusDTO status = adminStatusService.getStatusById(adminStatusId);
            if (status != null) {
                return ResponseEntity.ok(status);
            } else {
                return new ResponseEntity<>("Admin status not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the admin status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createStatus(@RequestBody AdminStatusRequest request) {
        try {
            AdminStatusDTO created = adminStatusService.saveStatus(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to create admin status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{adminStatusId}")
    public ResponseEntity<Object> updateStatus(@PathVariable Integer adminStatusId,
                                               @RequestBody AdminStatusRequest request) {
        try {
            AdminStatusDTO updated = adminStatusService.updateStatus(adminStatusId, request);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to update admin status.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{adminStatusId}")
    public ResponseEntity<Object> deleteStatus(@PathVariable Integer adminStatusId) {
        try {
            adminStatusService.deleteStatusById(adminStatusId);
            return ResponseEntity.ok("Admin status deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to delete admin status.", HttpStatus.BAD_REQUEST);
        }
    }
}