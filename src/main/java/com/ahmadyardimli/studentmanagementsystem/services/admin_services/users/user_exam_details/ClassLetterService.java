package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ClassLetterDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassLetter;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.ClassLetterDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.ClassLetterRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ClassLetterRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassLetterService {
    private final ClassLetterRepository classLetterRepository;
    private final ClassLetterDTOMapper classLetterDTOMapper;

    @Autowired
    public ClassLetterService(ClassLetterRepository classLetterRepository, ClassLetterDTOMapper classLetterDTOMapper) {
        this.classLetterRepository = classLetterRepository;
        this.classLetterDTOMapper = classLetterDTOMapper;
    }

    public List<ClassLetterDTO> getAllClassLetters() {
        List<ClassLetter> classLetters = classLetterRepository.findAll();
        return classLetters.stream()
                .map(classLetterDTOMapper)
                .collect(Collectors.toList());
    }

    public ClassLetterDTO createClassLetter(ClassLetterRequest classLetterRequest) {
        if (classLetterRequest.getLetterValue() == null || classLetterRequest.getLetterValue().trim().isEmpty()) {
            throw new RequestValidationException("Class letter cannot be empty.");
        }

        ValidationUtils.validateSingleLetter(classLetterRequest.getLetterValue());
        checkIfClassLetterAlreadyExists(classLetterRequest.getLetterValue());

        ClassLetter classLetter = new ClassLetter(classLetterRequest.getLetterValue());
        ClassLetter savedClassLetter = classLetterRepository.save(classLetter);
        return classLetterDTOMapper.apply(savedClassLetter);
    }

    // For controller: returns DTO
    public ClassLetterDTO getClassLetterById(Integer classLetterId) {
        checkIfClassLetterExistsOrThrow(classLetterId);
        return classLetterRepository.findById(classLetterId)
                .map(classLetterDTOMapper)
                .orElse(null);
    }

    // For services: returns Entity
    public ClassLetter getClassLetter(Integer classLetterId) {
        checkIfClassLetterExistsOrThrow(classLetterId);
        return classLetterRepository.findById(classLetterId).orElse(null);
    }

    public ClassLetterDTO updateClassLetter(Integer classLetterId, ClassLetterRequest classLetterRequest) {
        ClassLetter existingClassLetter = getClassLetter(classLetterId);
        boolean changes = false;

        String newLetterValue = classLetterRequest.getLetterValue();
        if (newLetterValue != null && !newLetterValue.trim().isEmpty()
                && !newLetterValue.equals(existingClassLetter.getLetterValue())) {

            ValidationUtils.validateSingleLetter(newLetterValue);

            checkIfClassLetterAlreadyExistsWithCaseSensitivity(newLetterValue, existingClassLetter.getLetterValue());
            existingClassLetter.setLetterValue(newLetterValue);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        ClassLetter updatedClassLetter = classLetterRepository.save(existingClassLetter);
        return classLetterDTOMapper.apply(updatedClassLetter);
    }
    public void deleteClassLetterById(Integer classLetterId) {
        checkIfClassLetterExistsOrThrow(classLetterId);
        classLetterRepository.deleteById(classLetterId);
    }
    private void checkIfClassLetterAlreadyExists(String letterValue) {
        ClassLetter found = classLetterRepository.findByLetterValue(letterValue);
        if (found != null) {
            throw new ResourceAlreadyExistsException(
                    "The class letter '" + letterValue + "' already exists."
            );
        }
    }
    private void checkIfClassLetterExistsOrThrow(int classLetterId) {
        if (!classLetterRepository.existsById(classLetterId)) {
            throw new ResourceNotFoundException("ClassLetter with ID " + classLetterId + " not found");
        }
    }
    private void checkIfClassLetterAlreadyExistsWithCaseSensitivity(String newValue, String currentValue) {
        ClassLetter found = classLetterRepository.findByLetterValue(newValue);
        if (found != null && !found.getLetterValue().equalsIgnoreCase(currentValue)) {
            throw new ResourceAlreadyExistsException(
                    "The class letter '" + newValue + "' already exists."
            );
        }
    }
}
