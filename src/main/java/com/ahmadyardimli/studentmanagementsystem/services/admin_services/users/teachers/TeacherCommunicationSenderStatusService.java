package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.TeacherCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.teacher_mappers.TeacherCommunicationSenderStatusDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers.TeacherCommunicationSenderStatusRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.TeacherCommunicationSenderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherCommunicationSenderStatusService {
    private final TeacherCommunicationSenderStatusRepository teacherCommunicationSenderStatusRepository;
    private final TeacherCommunicationSenderStatusDTOMapper teacherCommunicationSenderStatusDTOMapper;

    @Autowired
    public TeacherCommunicationSenderStatusService(TeacherCommunicationSenderStatusRepository teacherCommunicationSenderStatusRepository, TeacherCommunicationSenderStatusDTOMapper teacherCommunicationSenderStatusDTOMapper) {
        this.teacherCommunicationSenderStatusRepository = teacherCommunicationSenderStatusRepository;
        this.teacherCommunicationSenderStatusDTOMapper = teacherCommunicationSenderStatusDTOMapper;
    }

    public List<TeacherCommunicationSenderStatusDTO> getAllStatuses() {
        List<TeacherCommunicationSenderStatus> teacherCommunicationSenderStatuses = teacherCommunicationSenderStatusRepository.findAll();
        return teacherCommunicationSenderStatuses.stream()
                .map(teacherCommunicationSenderStatusDTOMapper)
                .collect(Collectors.toList());
    }

    public TeacherCommunicationSenderStatusDTO saveStatus(TeacherCommunicationSenderStatusRequest teacherCommunicationSenderStatusRequest) {
        // Check if the communication sender status with the given status already exists
        checkIfStatusAlreadyExists(teacherCommunicationSenderStatusRequest.getStatus());

        TeacherCommunicationSenderStatus teacherCommunicationSenderStatus = new TeacherCommunicationSenderStatus(teacherCommunicationSenderStatusRequest.getStatus());
        TeacherCommunicationSenderStatus savedTeacherCommunicationSenderStatus = teacherCommunicationSenderStatusRepository.save(teacherCommunicationSenderStatus);
        return teacherCommunicationSenderStatusDTOMapper.apply(savedTeacherCommunicationSenderStatus);
    }

    private void checkIfStatusAlreadyExists(String status) {
        TeacherCommunicationSenderStatus teacherCommunicationSenderStatus = teacherCommunicationSenderStatusRepository.findByStatus(status);
        if (teacherCommunicationSenderStatus != null) {
            throw new ResourceAlreadyExistsException("This status already exists.");
        }
    }

    // for controller, because it returns DTO
    public TeacherCommunicationSenderStatusDTO getCommunicationSenderStatusById(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        return teacherCommunicationSenderStatusRepository.findById(communicationSenderStatusId).map(teacherCommunicationSenderStatusDTOMapper).orElse(null);
    }

    public TeacherCommunicationSenderStatusDTO updateStatus(Integer communicationSenderStatusId, TeacherCommunicationSenderStatusRequest teacherCommunicationSenderStatusRequest) {
        TeacherCommunicationSenderStatus existingTeacherCommunicationSenderStatus = getCommunicationSenderStatus(communicationSenderStatusId);
        boolean changes = false;

        if (teacherCommunicationSenderStatusRequest.getStatus() != null && !teacherCommunicationSenderStatusRequest.getStatus().equals(existingTeacherCommunicationSenderStatus.getStatus())) {
            checkIfStatusAlreadyExists(teacherCommunicationSenderStatusRequest.getStatus());

            existingTeacherCommunicationSenderStatus.setStatus(teacherCommunicationSenderStatusRequest.getStatus());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        TeacherCommunicationSenderStatus updatedTeacherCommunicationSenderStatus = teacherCommunicationSenderStatusRepository.save(existingTeacherCommunicationSenderStatus);
        return teacherCommunicationSenderStatusDTOMapper.apply(updatedTeacherCommunicationSenderStatus);
    }

    // for services to get communication sender status
    public TeacherCommunicationSenderStatus getCommunicationSenderStatus(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        return teacherCommunicationSenderStatusRepository.findById(communicationSenderStatusId).orElse(null);
    }

    public void deleteStatusById(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        teacherCommunicationSenderStatusRepository.deleteById(communicationSenderStatusId);
    }

    private void checkIfStatusExistsOrThrow(int communicationSenderStatusId) {
        if (!teacherCommunicationSenderStatusRepository.existsById(communicationSenderStatusId)) {
            throw new ResourceNotFoundException("Communication sender status with an id [%s] not found".formatted(communicationSenderStatusId));
        }
    }
}
