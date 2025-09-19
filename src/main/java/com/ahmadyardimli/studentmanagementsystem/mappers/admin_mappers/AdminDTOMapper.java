package com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminRoleDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminStatusDTO;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class AdminDTOMapper implements Function<Admin, AdminDTO> {
    @Override
    public AdminDTO apply(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setLastLogin(admin.getLastLogin());
        adminDTO.setLoginAttempts(admin.getLoginAttempts());

        // Handle the AdminRole and AdminStatus conversion
        if (admin.getRole() != null) {
            adminDTO.setAdminRole(new AdminRoleDTO(admin.getRole().getId(), admin.getRole().getRole()));
        } else {
            adminDTO.setAdminRole(null);
        }

        if (admin.getStatus() != null) {
            adminDTO.setAdminStatus(new AdminStatusDTO(admin.getStatus().getId(), admin.getStatus().getStatus()));
        } else {
            adminDTO.setAdminStatus(null);
        }

        adminDTO.setCreatedAt(admin.getCreatedAt());
        adminDTO.setUpdatedAt(admin.getUpdatedAt());

        return adminDTO;
    }
}
