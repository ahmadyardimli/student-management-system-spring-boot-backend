package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.StudentCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.student_mappers.StudentCommunicationSenderStatusDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students.StudentCommunicationSenderStatusRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.students.StudentCommunicationSenderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCommunicationSenderStatusService {
    private final StudentCommunicationSenderStatusRepository studentCommunicationSenderStatusRepository;
    private final StudentCommunicationSenderStatusDTOMapper studentCommunicationSenderStatusDTOMapper;

    @Autowired
    public StudentCommunicationSenderStatusService(StudentCommunicationSenderStatusRepository studentCommunicationSenderStatusRepository, StudentCommunicationSenderStatusDTOMapper studentCommunicationSenderStatusDTOMapper) {
        this.studentCommunicationSenderStatusRepository = studentCommunicationSenderStatusRepository;
        this.studentCommunicationSenderStatusDTOMapper = studentCommunicationSenderStatusDTOMapper;
    }

    public List<StudentCommunicationSenderStatusDTO> getAllStatuses() {
        List<StudentCommunicationSenderStatus> studentCommunicationSenderStatuses = studentCommunicationSenderStatusRepository.findAll();
        return studentCommunicationSenderStatuses.stream()
                .map(studentCommunicationSenderStatusDTOMapper)
                .collect(Collectors.toList());
    }

    public StudentCommunicationSenderStatusDTO saveStatus(StudentCommunicationSenderStatusRequest studentCommunicationSenderStatusRequest) {
        checkIfStatusAlreadyExists(studentCommunicationSenderStatusRequest.getStatus());

        StudentCommunicationSenderStatus studentCommunicationSenderStatus = new StudentCommunicationSenderStatus(studentCommunicationSenderStatusRequest.getStatus());
        StudentCommunicationSenderStatus savedStudentCommunicationSenderStatus = studentCommunicationSenderStatusRepository.save(studentCommunicationSenderStatus);
        return studentCommunicationSenderStatusDTOMapper.apply(savedStudentCommunicationSenderStatus);
    }

    private void checkIfStatusAlreadyExists(String status) {
        StudentCommunicationSenderStatus studentCommunicationSenderStatus = studentCommunicationSenderStatusRepository.findByStatus(status);
        if (studentCommunicationSenderStatus != null) {
            throw new ResourceAlreadyExistsException("This status already exists.");
        }
    }

    // for controller, because it returns DTO
    public StudentCommunicationSenderStatusDTO getStudentCommunicationSenderStatusById(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        return studentCommunicationSenderStatusRepository.findById(communicationSenderStatusId).map(studentCommunicationSenderStatusDTOMapper).orElse(null);
    }

    public StudentCommunicationSenderStatusDTO updateStatus(Integer communicationSenderStatusId, StudentCommunicationSenderStatusRequest studentCommunicationSenderStatusRequest) {
        StudentCommunicationSenderStatus existingStudentCommunicationSenderStatus = getStudentCommunicationSenderStatus(communicationSenderStatusId);
        boolean changes = false;

        if (studentCommunicationSenderStatusRequest.getStatus() != null && !studentCommunicationSenderStatusRequest.getStatus().equals(existingStudentCommunicationSenderStatus.getStatus())) {
            checkIfStatusAlreadyExists(studentCommunicationSenderStatusRequest.getStatus());

            existingStudentCommunicationSenderStatus.setStatus(studentCommunicationSenderStatusRequest.getStatus());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        StudentCommunicationSenderStatus updatedStudentCommunicationSenderStatus = studentCommunicationSenderStatusRepository.save(existingStudentCommunicationSenderStatus);
        return studentCommunicationSenderStatusDTOMapper.apply(updatedStudentCommunicationSenderStatus);
    }

    // for services to get communication sender status
    public StudentCommunicationSenderStatus getStudentCommunicationSenderStatus(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        return studentCommunicationSenderStatusRepository.findById(communicationSenderStatusId).orElse(null);
    }

    public void deleteStatusById(Integer communicationSenderStatusId) {
        checkIfStatusExistsOrThrow(communicationSenderStatusId);
        studentCommunicationSenderStatusRepository.deleteById(communicationSenderStatusId);
    }

    private void checkIfStatusExistsOrThrow(int communicationSenderStatusId) {
        if (!studentCommunicationSenderStatusRepository.existsById(communicationSenderStatusId)) {
            throw new ResourceNotFoundException("Communication sender status with an id [%s] not found".formatted(communicationSenderStatusId));
        }
    }
}
