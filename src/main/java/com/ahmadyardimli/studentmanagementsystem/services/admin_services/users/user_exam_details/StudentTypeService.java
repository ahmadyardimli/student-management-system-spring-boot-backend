package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.StudentTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.StudentType;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.StudentTypeDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.StudentTypeRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.StudentTypeRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentTypeService {
    private final StudentTypeRepository studentTypeRepository;
    private final StudentTypeDTOMapper studentTypeDTOMapper;

    @Autowired
    public StudentTypeService(StudentTypeRepository studentTypeRepository, StudentTypeDTOMapper studentTypeDTOMapper) {
        this.studentTypeRepository = studentTypeRepository;
        this.studentTypeDTOMapper = studentTypeDTOMapper;
    }

    public List<StudentTypeDTO> getAllStudentTypes() {
        List<StudentType> studentTypes = studentTypeRepository.findAll();
        return studentTypes.stream()
                .map(studentTypeDTOMapper)
                .collect(Collectors.toList());
    }

    public StudentTypeDTO createStudentType(StudentTypeRequest studentTypeRequest) {
        ValidationUtils.validateSingleSpace(studentTypeRequest.getType());
        checkIfStudentTypeAlreadyExists(studentTypeRequest.getType());

        StudentType studentType = new StudentType(studentTypeRequest.getType());
        StudentType savedStudentType = studentTypeRepository.save(studentType);
        return studentTypeDTOMapper.apply(savedStudentType);
    }

    // For controller, because it returns DTO
    public StudentTypeDTO getStudentTypeById(Integer studentTypeId) {
        checkIfStudentTypeExistsOrThrow(studentTypeId);
        return studentTypeRepository.findById(studentTypeId).map(studentTypeDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public StudentType getStudentType(Integer studentTypeId){
        checkIfStudentTypeExistsOrThrow(studentTypeId);
        return studentTypeRepository.findById(studentTypeId).orElse(null);
    }

    public StudentTypeDTO updateStudentType(Integer studentTypeId, StudentTypeRequest studentTypeRequest) {
        StudentType existingStudentType = getStudentType(studentTypeId);
        boolean changes = false;

        if (studentTypeRequest.getType() != null && !studentTypeRequest.getType().equals(existingStudentType.getType())) {
            ValidationUtils.validateSingleSpace(studentTypeRequest.getType());
            checkIfStudentTypeAlreadyExistsWithCaseSensitivity(studentTypeRequest.getType(), existingStudentType.getType());
            existingStudentType.setType(studentTypeRequest.getType());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        StudentType updatedStudentType = studentTypeRepository.save(existingStudentType);
        return studentTypeDTOMapper.apply(updatedStudentType);
    }

    private void checkIfStudentTypeAlreadyExistsWithCaseSensitivity(String newType, String currentType) {
        StudentType foundStudentType = studentTypeRepository.findByTypeIgnoreCase(newType);
        if (foundStudentType != null && !foundStudentType.getType().equalsIgnoreCase(currentType)) {
            throw new ResourceAlreadyExistsException("The student type '" + newType + "' already exists.");
        }
    }

    public void deleteStudentTypeById(Integer studentTypeId) {
        checkIfStudentTypeExistsOrThrow(studentTypeId);
        studentTypeRepository.deleteById(studentTypeId);
    }

    private void checkIfStudentTypeExistsOrThrow(int studentTypeId) {
        if (!studentTypeRepository.existsById(studentTypeId)) {
            throw new ResourceNotFoundException("StudentType with ID " + studentTypeId + " not found");
        }
    }

    private void checkIfStudentTypeAlreadyExists(String name) {
        StudentType studentType = studentTypeRepository.findByType(name);
        if (studentType != null) {
            throw new ResourceAlreadyExistsException("The student type '" + name + "' already exists.");
        }
    }
}
