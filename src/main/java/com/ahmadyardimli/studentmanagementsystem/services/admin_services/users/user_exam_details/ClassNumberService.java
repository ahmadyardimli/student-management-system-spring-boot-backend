package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ClassNumberDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassNumber;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.ClassNumberDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.ClassNumberRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ClassNumberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassNumberService {
    private final ClassNumberRepository classNumberRepository;
    private final ClassNumberDTOMapper classNumberDTOMapper;

    @Autowired
    public ClassNumberService(ClassNumberRepository classNumberRepository, ClassNumberDTOMapper classNumberDTOMapper) {
        this.classNumberRepository = classNumberRepository;
        this.classNumberDTOMapper = classNumberDTOMapper;
    }

    public List<ClassNumberDTO> getAllClassNumbers() {
        List<ClassNumber> classNumbers = classNumberRepository.findAll();
        return classNumbers.stream()
                .map(classNumberDTOMapper)
                .collect(Collectors.toList());
    }

    public ClassNumberDTO createClassNumber(ClassNumberRequest classNumberRequest) {
        // ensure numberValue > 0
        if (classNumberRequest.getNumberValue() <= 0) {
            throw new RequestValidationException("Class number must be greater than 0.");
        }

        checkIfClassNumberAlreadyExists(classNumberRequest.getNumberValue());

        ClassNumber classNumber = new ClassNumber(classNumberRequest.getNumberValue());
        ClassNumber savedClassNumber = classNumberRepository.save(classNumber);
        return classNumberDTOMapper.apply(savedClassNumber);
    }

    // For controller, returns DTO
    public ClassNumberDTO getClassNumberById(Integer classNumberId) {
        checkIfClassNumberExistsOrThrow(classNumberId);
        return classNumberRepository.findById(classNumberId)
                .map(classNumberDTOMapper)
                .orElse(null);
    }

    // For services, returns Entity
    public ClassNumber getClassNumber(Integer classNumberId) {
        checkIfClassNumberExistsOrThrow(classNumberId);
        return classNumberRepository.findById(classNumberId).orElse(null);
    }

    public ClassNumberDTO updateClassNumber(Integer classNumberId, ClassNumberRequest classNumberRequest) {
        ClassNumber existingClassNumber = getClassNumber(classNumberId);
        boolean changes = false;

        if (classNumberRequest.getNumberValue() != 0 && classNumberRequest.getNumberValue() != existingClassNumber.getNumberValue()) {
            // If I want to re-check domain constraints
            if (classNumberRequest.getNumberValue() <= 0) {
                throw new RequestValidationException("Class number must be greater than 0.");
            }
            checkIfClassNumberAlreadyExistsWithDifferentValue(
                    classNumberRequest.getNumberValue(), existingClassNumber.getNumberValue()
            );
            existingClassNumber.setNumberValue(classNumberRequest.getNumberValue());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        ClassNumber updatedClassNumber = classNumberRepository.save(existingClassNumber);
        return classNumberDTOMapper.apply(updatedClassNumber);
    }

    public void deleteClassNumberById(Integer classNumberId) {
        checkIfClassNumberExistsOrThrow(classNumberId);
        classNumberRepository.deleteById(classNumberId);
    }

    private void checkIfClassNumberAlreadyExists(int numberValue) {
        boolean exists = classNumberRepository.existsByNumberValue(numberValue);
        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "The class number '" + numberValue + "' already exists."
            );
        }
    }

    private void checkIfClassNumberExistsOrThrow(int classNumberId) {
        if (!classNumberRepository.existsById(classNumberId)) {
            throw new ResourceNotFoundException("ClassNumber with ID " + classNumberId + " not found");
        }
    }

    private void checkIfClassNumberAlreadyExistsWithDifferentValue(int newValue, int currentValue) {
        // If the newValue already exists and not the same as currentValue
        ClassNumber found = classNumberRepository.findByNumberValue(newValue);
        if (found != null && found.getNumberValue() != currentValue) {
            throw new ResourceAlreadyExistsException(
                    "The class number '" + newValue + "' already exists."
            );
        }
    }
}
