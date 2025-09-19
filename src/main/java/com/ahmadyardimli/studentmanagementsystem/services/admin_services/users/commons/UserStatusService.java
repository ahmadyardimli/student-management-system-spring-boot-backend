package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserStatus;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers.UserStatusDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserStatusRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserStatusDTOMapper userStatusDTOMapper;

    @Autowired
    public UserStatusService(UserStatusRepository userStatusRepository, UserStatusDTOMapper userStatusDTOMapper) {
        this.userStatusRepository = userStatusRepository;
        this.userStatusDTOMapper = userStatusDTOMapper;
    }

    public List<UserStatusDTO> getAllStatuses() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatuses.stream()
                .map(userStatusDTOMapper)
                .collect(Collectors.toList());
    }

    public UserStatusDTO saveStatus(UserStatusRequest userStatusRequest) {
        checkIfStatusAlreadyExists(userStatusRequest.getStatus());

        UserStatus userStatus = new UserStatus(userStatusRequest.getStatus());
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        return userStatusDTOMapper.apply(savedUserStatus);
    }

    private void checkIfStatusAlreadyExists(String status) {
        UserStatus userStatus = userStatusRepository.findByStatus(status);
        if (userStatus != null) throw new ResourceAlreadyExistsException("User status already exists.");
    }

    // for controller, because it returns DTO
    public UserStatusDTO getStatusById(Integer userStatusId) {
        checkIfStatusExistsOrThrow(userStatusId);
        return userStatusRepository.findById(userStatusId).map(userStatusDTOMapper).orElse(null);
    }

    public UserStatusDTO updateStatus(Integer userStatusId, UserStatusRequest userStatusRequest) {
        UserStatus existingUserStatus = getStatus(userStatusId);
        boolean changes = false;

        if (userStatusRequest.getStatus() != null && !userStatusRequest.getStatus().equals(existingUserStatus.getStatus())){
            checkIfStatusAlreadyExists(userStatusRequest.getStatus());

            existingUserStatus.setStatus(userStatusRequest.getStatus());
            changes = true;
        }

        if (!changes)
            throw new RequestValidationException("No changes were made.");

        UserStatus updatedUserStatus = userStatusRepository.save(existingUserStatus);
        return userStatusDTOMapper.apply(updatedUserStatus);
    }

    // for services to get User status
    public UserStatus getStatus(Integer userStatusId) {
        checkIfStatusExistsOrThrow(userStatusId);
        return userStatusRepository.findById(userStatusId).orElse(null);
    }

    public void deleteStatusById(Integer userStatusId) {
        checkIfStatusExistsOrThrow(userStatusId);
        userStatusRepository.deleteById(userStatusId);
    }

    private void checkIfStatusExistsOrThrow(int userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)){
            throw new ResourceNotFoundException("User status with ID [%s] not found".formatted(userStatusId));
        }
    }
}
