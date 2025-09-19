package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SubjectDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.SubjectDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.SubjectRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SubjectRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectDTOMapper subjectDTOMapper;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, SubjectDTOMapper subjectDTOMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectDTOMapper = subjectDTOMapper;
    }

    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subjectDTOMapper)
                .collect(Collectors.toList());
    }

    public SubjectDTO createSubject(SubjectRequest subjectRequest) {
        ValidationUtils.validateSingleSpace(subjectRequest.getSubject());
        // Check if the subject with the given name already exists
        checkIfSubjectAlreadyExists(subjectRequest.getSubject());

        Subject subject = new Subject(subjectRequest.getSubject());
        Subject savedSubject = subjectRepository.save(subject);
        return subjectDTOMapper.apply(savedSubject);
    }

    // For controller, because it returns DTO
    public SubjectDTO getSubjectById(Integer subjectId) {
        checkIfSubjectExistsOrThrow(subjectId);
        return subjectRepository.findById(subjectId).map(subjectDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public Subject getSubject(Integer subjectId){
        checkIfSubjectExistsOrThrow(subjectId);
        return subjectRepository.findById(subjectId).orElse(null);
    }

    public SubjectDTO updateSubject(Integer subjectId, SubjectRequest subjectRequest) {
        Subject existingSubject = getSubject(subjectId);
        boolean changes = false;

        if (subjectRequest.getSubject() != null && !subjectRequest.getSubject().equals(existingSubject.getSubject())) {
            ValidationUtils.validateSingleSpace(subjectRequest.getSubject());
            checkIfSubjectAlreadyExistsWithCaseSensitivity(subjectRequest.getSubject(), existingSubject.getSubject());
            existingSubject.setSubject(subjectRequest.getSubject());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        Subject updatedSubject = subjectRepository.save(existingSubject);
        return subjectDTOMapper.apply(updatedSubject);
    }

    public void deleteSubjectById(Integer subjectId) {
        checkIfSubjectExistsOrThrow(subjectId);
        subjectRepository.deleteTeacherSubjectBySubjectId(subjectId);
        subjectRepository.deleteById(subjectId);
    }

    private void checkIfSubjectAlreadyExistsWithCaseSensitivity(String newName, String currentName) {
        Subject foundSubject = subjectRepository.findBySubjectIgnoreCase(newName);
        if (foundSubject != null && !foundSubject.getSubject().equalsIgnoreCase(currentName)) {
            throw new ResourceAlreadyExistsException("The subject '" + newName + "' already exists.");
        }
    }

    private void checkIfSubjectExistsOrThrow(int subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException("Subject not found.");
        }
    }

    private void checkIfSubjectAlreadyExists(String name) {
        Subject subject = subjectRepository.findBySubject(name);
        if (subject != null) {
            throw new ResourceAlreadyExistsException("The subject '" + name + "' already exists.");
        }
    }
}
