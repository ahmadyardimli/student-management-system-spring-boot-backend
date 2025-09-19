package com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRole;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminRoleDTO;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class AdminRoleDTOMapper implements Function<AdminRole, AdminRoleDTO> {
    @Override
    public AdminRoleDTO apply(AdminRole adminRole) {
        return new AdminRoleDTO(
                adminRole.getId(),
                adminRole.getRole()
        );
    }
}
