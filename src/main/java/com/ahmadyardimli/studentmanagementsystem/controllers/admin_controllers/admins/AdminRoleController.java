package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.admins;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminRoleDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRole;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminRoleRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins.AdminRoleService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/admin-roles")
public class AdminRoleController {
    private final AdminRoleService adminRoleService;

    @Autowired
    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAdminRoles() {
        List<AdminRoleDTO> adminRoles = adminRoleService.getAllRoles();
        try {
            if (!adminRoles.isEmpty()) {
                return ResponseEntity.ok(adminRoles);
            } else {
                return new ResponseEntity<>("No admin roles found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching admin roles.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-admin-role")
    public ResponseEntity<Object> createAdminRole(@RequestBody AdminRoleRequest adminRoleRequest) {
        try {
            AdminRoleDTO newAdminRole = adminRoleService.saveRole(adminRoleRequest);
            if (newAdminRole != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newAdminRole);
            } else {
                return new ResponseEntity<>("Failed to create admin role.", HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-admin-role/{adminRoleId}")
    public ResponseEntity<Object> updateAdminRole(
            @PathVariable Integer adminRoleId,
            @RequestBody AdminRoleRequest adminRoleRequest) {
        try {
            AdminRoleDTO updatedAdminRole = adminRoleService.updateRole(adminRoleId, adminRoleRequest);
            if (updatedAdminRole != null) {
                return new ResponseEntity<>(updatedAdminRole, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update admin role.", HttpStatus.BAD_REQUEST);
            }
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{adminRoleId}")
    public ResponseEntity<Object> getAdminRole(@PathVariable Integer adminRoleId) {
        try {
            AdminRoleDTO adminRole = adminRoleService.getRoleById(adminRoleId);
            if (adminRole != null) {
                return ResponseEntity.ok(adminRole);
            } else {
                return new ResponseEntity<>("Admin role not found.", HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the admin role.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{adminRoleId}")
    public ResponseEntity<Object> deleteAdminRole(@PathVariable Integer adminRoleId) {
        try {
            adminRoleService.deleteRoleById(adminRoleId);

            AdminRole adminRole = adminRoleService.getRole(adminRoleId);

            if (adminRole != null){
                List<Admin> admins = adminRole.getAdmins();

                for (Admin admin : admins)
                    admin.setRole(null);
            }

            return new ResponseEntity<>("Admin role deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to delete admin role.", HttpStatus.BAD_REQUEST);
        }
    }
}
