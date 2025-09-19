package com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins;

import com.ahmadyardimli.studentmanagementsystem.responses.AuthResponse;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRole;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminStatus;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers.AdminDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.admin_repos.AdminRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminRequest;
import com.ahmadyardimli.studentmanagementsystem.security.JwtTokenProvider;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.AuthService;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.RefreshTokenService;
import com.ahmadyardimli.studentmanagementsystem.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService extends AuthService<Admin> {
    private final AdminRepository adminRepository;
    private final AdminDTOMapper adminDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminRoleService adminRoleService;
    private final AdminStatusService adminStatusService;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            AdminDTOMapper adminDTOMapper,
            PasswordEncoder passwordEncoder,
            AdminRoleService adminRoleService,
            AdminStatusService adminStatusService,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            RefreshTokenService refreshTokenService) {
        super(jwtTokenProvider, authenticationManager, refreshTokenService);
        this.adminRepository = adminRepository;
        this.adminDTOMapper = adminDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminRoleService = adminRoleService;
        this.adminStatusService = adminStatusService;
    }

    public AuthResponse registerAdmin(AdminRequest adminRequest) {
        checkIfUsernameAlreadyExists(adminRequest.getUsername());
        checkIfMainAdminAlreadyExists(adminRequest.getAdminRoleId());

        String firstName = adminRequest.getFirstName();
        String lastName  = adminRequest.getLastName();
        String username = adminRequest.getUsername();
        String email = adminRequest.getEmail();
        String password = adminRequest.getPassword();
        int adminRoleId = adminRequest.getAdminRoleId();
        int adminStatusId = adminRequest.getAdminStatusId();

        // Extract authKey and verificationToken from private methods
        String authKey = SecurityUtils.generateUniqueAuthKey();
        String verificationToken = SecurityUtils.generateUniqueVerificationToken();

        // Extract relational data
        AdminStatus adminStatus = adminStatusService.getStatus(adminStatusId);
        AdminRole adminRole = adminRoleService.getRole(adminRoleId);

        // Set admin to upcoming User Request values
        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(adminRole);
        admin.setStatus(adminStatus);
        admin.setAuthKey(authKey);
        admin.setVerificationToken(verificationToken);
        admin.setLastLogin(LocalDateTime.now());
        admin.setLoginAttempts(0); // TODO: later implement logic for login attempts

        Admin savedAdmin = saveEntity(admin);
        return register(savedAdmin, username, password);
    }

    public AdminDTO updateAdmin(int adminId, AdminRequest adminRequest) {
        checkIfAdminExistsOrThrow(adminId);

        Admin existingAdmin = getAdmin(adminId);
        boolean changes = false;

        if (adminRequest.getFirstName() != null
                && !adminRequest.getFirstName().equals(existingAdmin.getFirstName())) {
            existingAdmin.setFirstName(adminRequest.getFirstName());
            changes = true;
        }

        if (adminRequest.getLastName() != null
                && !adminRequest.getLastName().equals(existingAdmin.getLastName())) {
            existingAdmin.setLastName(adminRequest.getLastName());
            changes = true;
        }


        if (adminRequest.getUsername() != null && !adminRequest.getUsername().equals(existingAdmin.getUsername())){
            existingAdmin.setUsername(adminRequest.getUsername());
            changes = true;
        }

        if (adminRequest.getPassword() != null){
            if (!passwordEncoder.matches(adminRequest.getPassword(), existingAdmin.getPasswordHash())) {
                String newPasswordHash = passwordEncoder.encode(adminRequest.getPassword());
                existingAdmin.setPasswordHash(newPasswordHash);
                changes = true;
            }
        }

        if (adminRequest.getEmail() != null && !adminRequest.getEmail().equals(existingAdmin.getEmail())){
            existingAdmin.setEmail(adminRequest.getEmail());
            changes = true;
        }

        if (adminRequest.getAdminRoleId() != 0 && adminRequest.getAdminRoleId() != existingAdmin.getRole().getId()){
            checkIfMainAdminAlreadyExists(adminRequest.getAdminRoleId());

            AdminRole adminRole = adminRoleService.getRole(adminRequest.getAdminRoleId());
            existingAdmin.setRole(adminRole);
            changes = true;
        }

        if (adminRequest.getAdminStatusId() != 0 && adminRequest.getAdminStatusId() != existingAdmin.getStatus().getId()){
            AdminStatus adminStatus = adminStatusService.getStatus(adminRequest.getAdminStatusId());
            existingAdmin.setStatus(adminStatus);
            changes = true;
        }

        if (!changes)
            throw new RequestValidationException("No changes were made.");

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return adminDTOMapper.apply(updatedAdmin);
    }

    public List<AdminDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(adminDTOMapper)
                .collect(Collectors.toList());
    }

    public AdminDTO getAdminById(int adminId) {
        checkIfAdminExistsOrThrow(adminId);
        return adminRepository.findById(adminId)
                .map(adminDTOMapper)
                .orElse(null);
    }

    public Admin getAdminByUsername(String username){
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null) {
            return admin;
        } else {
            return null;
        }
    }

    public Admin getAdmin(int adminId) {
        checkIfAdminExistsOrThrow(adminId);
        return adminRepository.findById(adminId).orElse(null);
    }

    public void deleteAdminById(int adminId) {
        checkIfAdminExistsOrThrow(adminId);
        adminRepository.deleteById(adminId);
    }

    private void checkIfAdminExistsOrThrow(int adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new ResourceNotFoundException("Admin not found.");
        }
    }

    public void checkIfUsernameAlreadyExists(String username) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            throw new ResourceAlreadyExistsException("Admin username already exists.");
        }
    }

    private void checkIfMainAdminAlreadyExists(int adminRoleId) {
        Admin admin = adminRepository.findByAdminRoleId(adminRoleId);
        if (admin != null && admin.getRole().getId() == 1) {
            throw new ResourceAlreadyExistsException("Main admin already exists.");
        }
    }

    public Admin saveEntity(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin findAdminOrNull(int id) { return adminRepository.findById(id).orElse(null); }
}
