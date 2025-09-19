package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.admins;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminDTO;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminRequest;
import com.ahmadyardimli.studentmanagementsystem.responses.AuthResponse;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins.AdminService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            AuthResponse authResponse = adminService.registerAdmin(adminRequest);
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

    @PutMapping("/update-admin/{adminId}")
    public ResponseEntity<Object> updateAdmin(@PathVariable int adminId, @RequestBody AdminRequest adminRequest) {
        try {
            AdminDTO updatedUser = adminService.updateAdmin(adminId, adminRequest);
            if (updatedUser != null)
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            else return new ResponseEntity<>("Failed to update admin.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllAdmins() {
        List<AdminDTO> admins = adminService.getAllAdmins();
        try {
            if (!admins.isEmpty())
                return ResponseEntity.ok(admins);
            else
                return new ResponseEntity<>("No admins found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            return new ResponseEntity<>("An error occurred while fetching admins.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<Object> getOneAdmin(@PathVariable("adminId") Integer adminId) {
        try {
            AdminDTO admin = adminService.getAdminById(adminId);
            if (admin != null)
                return ResponseEntity.ok(admin);
            else
                return new ResponseEntity<>("Admin not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the admin.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable("adminId") Integer adminId) {
        try {
            adminService.deleteAdminById(adminId);
            return ResponseEntity.ok("Admin deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to delete admin.", HttpStatus.BAD_REQUEST);
        }
    }
}
