package com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins;

import com.ahmadyardimli.studentmanagementsystem.dtos.admin_dtos.AdminStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminStatus;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.admin_mappers.AdminStatusDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.admin_repos.AdminStatusRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.admin_requests.AdminStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminStatusService {
    private final AdminStatusRepository adminStatusRepository;
    private final AdminStatusDTOMapper adminStatusDTOMapper;

    @Autowired
    public AdminStatusService(AdminStatusRepository adminStatusRepository, AdminStatusDTOMapper adminStatusDTOMapper) {
        this.adminStatusRepository = adminStatusRepository;
        this.adminStatusDTOMapper = adminStatusDTOMapper;
    }

    public List<AdminStatusDTO> getAllStatuses() {
        List<AdminStatus> adminStatuses = adminStatusRepository.findAll();
        return adminStatuses.stream()
                .map(adminStatusDTOMapper)
                .collect(Collectors.toList());
    }

    public AdminStatusDTO saveStatus(AdminStatusRequest adminStatusRequest) {
        // Check if the admin status with the given status already exists
        checkIfStatusAlreadyExists(adminStatusRequest.getStatus());

        AdminStatus adminStatus = new AdminStatus(adminStatusRequest.getStatus());
        AdminStatus savedAdminStatus = adminStatusRepository.save(adminStatus);
        return adminStatusDTOMapper.apply(savedAdminStatus);
    }

    private void checkIfStatusAlreadyExists(String status) {
        AdminStatus adminStatus = adminStatusRepository.findByStatus(status);
        if (adminStatus != null) throw new ResourceAlreadyExistsException("Admin status already exists.");
    }

    // for controller, because it returns DTO
    public AdminStatusDTO getStatusById(Integer adminStatusId) {
        checkIfStatusExistsOrThrow(adminStatusId);
        return adminStatusRepository.findById(adminStatusId).map(adminStatusDTOMapper).orElse(null);
    }

    public AdminStatusDTO updateStatus(Integer adminStatusId, AdminStatusRequest adminStatusRequest) {
        AdminStatus existingAdminStatus = getStatus(adminStatusId);
        boolean changes = false;

        if (adminStatusRequest.getStatus() != null && !adminStatusRequest.getStatus().equals(existingAdminStatus.getStatus())){
            checkIfStatusAlreadyExists(adminStatusRequest.getStatus());

            existingAdminStatus.setStatus(adminStatusRequest.getStatus());
            changes = true;
        }

        if (!changes)
            throw new RequestValidationException("No changes were made.");

        AdminStatus updatedAdminStatus = adminStatusRepository.save(existingAdminStatus);
        return adminStatusDTOMapper.apply(updatedAdminStatus);
    }

    // for services to get Admin status
    public AdminStatus getStatus(Integer adminStatusId) {
        checkIfStatusExistsOrThrow(adminStatusId);
        return adminStatusRepository.findById(adminStatusId).orElse(null);
    }

    public void deleteStatusById(Integer adminStatusId) {
        checkIfStatusExistsOrThrow(adminStatusId);
        adminStatusRepository.deleteById(adminStatusId);
    }

    private void checkIfStatusExistsOrThrow(int adminStatusId) {
        if (!adminStatusRepository.existsById(adminStatusId)){
            throw new ResourceNotFoundException("Admin status with an id [%s] not found".formatted(adminStatusId));
        }
    }
}
