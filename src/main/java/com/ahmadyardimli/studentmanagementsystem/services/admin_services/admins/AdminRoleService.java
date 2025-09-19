package com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins;

import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminRoleDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRole;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers.AdminRoleDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.admin_repos.AdminRoleRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminRoleService {
    private final AdminRoleRepository adminRoleRepository;
    private final AdminRoleDTOMapper adminRoleDTOMapper;

    @Autowired
    public AdminRoleService(AdminRoleRepository adminRoleRepository, AdminRoleDTOMapper adminRoleDTOMapper) {
        this.adminRoleRepository = adminRoleRepository;
        this.adminRoleDTOMapper = adminRoleDTOMapper;
    }

    public List<AdminRoleDTO> getAllRoles() {
        List<AdminRole> adminRoles = adminRoleRepository.findAll();
        return adminRoles.stream()
                .map(adminRoleDTOMapper)
                .collect(Collectors.toList());
    }

    public AdminRoleDTO saveRole(AdminRoleRequest adminRoleRequest) {
        // Check if the admin role with the given role already exists
        checkIfRoleAlreadyExists(adminRoleRequest.getRole());

        AdminRole adminRole = new AdminRole(adminRoleRequest.getRole());
        AdminRole savedAdminRole = adminRoleRepository.save(adminRole);
        return adminRoleDTOMapper.apply(savedAdminRole);
    }

    private void checkIfRoleAlreadyExists(String role) {
        AdminRole adminRole = adminRoleRepository.findByRole(role);
        if (adminRole != null) {
            throw new ResourceAlreadyExistsException("Admin role already exists.");
        }
    }

    // for controller, because it returns DTO
    public AdminRoleDTO getRoleById(Integer adminRoleId) {
        checkIfRoleExistsOrThrow(adminRoleId);
        return adminRoleRepository.findById(adminRoleId).map(adminRoleDTOMapper).orElse(null);
    }

    public AdminRoleDTO updateRole(Integer adminRoleId, AdminRoleRequest adminRoleRequest) {
        AdminRole existingAdminRole = getRole(adminRoleId);
        boolean changes = false;

        if (adminRoleRequest.getRole() != null && !adminRoleRequest.getRole().equals(existingAdminRole.getRole())){
            checkIfRoleAlreadyExists(adminRoleRequest.getRole());

            existingAdminRole.setRole(adminRoleRequest.getRole());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        AdminRole updatedAdminRole = adminRoleRepository.save(existingAdminRole);
        return adminRoleDTOMapper.apply(updatedAdminRole);
    }

    // for services to get Admin role
    public AdminRole getRole(Integer adminRoleId) {
        checkIfRoleExistsOrThrow(adminRoleId);
        return adminRoleRepository.findById(adminRoleId).orElse(null);
    }

    public void deleteRoleById(Integer adminRoleId) {
        checkIfRoleExistsOrThrow(adminRoleId);
        adminRoleRepository.deleteById(adminRoleId);
    }

    private void checkIfRoleExistsOrThrow(int adminRoleId) {
        if (!adminRoleRepository.existsById(adminRoleId)){
            throw new ResourceNotFoundException("Admin role with an id [%s] not found".formatted(adminRoleId));
        }
    }
}
