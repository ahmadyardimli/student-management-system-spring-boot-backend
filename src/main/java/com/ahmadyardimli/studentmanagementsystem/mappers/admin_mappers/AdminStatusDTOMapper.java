package com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminStatus;
import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminStatusDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AdminStatusDTOMapper implements Function<AdminStatus, AdminStatusDTO> {
    @Override
    public AdminStatusDTO apply(AdminStatus adminStatus) {
        return new AdminStatusDTO(
                adminStatus.getId(),
                adminStatus.getStatus()
        );
    }
}
