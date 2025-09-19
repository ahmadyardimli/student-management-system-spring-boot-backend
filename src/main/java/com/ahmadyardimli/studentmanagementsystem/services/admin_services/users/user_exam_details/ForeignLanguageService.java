package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ForeignLanguageDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ForeignLanguage;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.ForeignLanguageDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.ForeignLanguageRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.ForeignLanguageRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForeignLanguageService {
    private final ForeignLanguageRepository foreignLanguageRepository;
    private final ForeignLanguageDTOMapper foreignLanguageDTOMapper;

    @Autowired
    public ForeignLanguageService(ForeignLanguageRepository foreignLanguageRepository, ForeignLanguageDTOMapper foreignLanguageDTOMapper) {
        this.foreignLanguageRepository = foreignLanguageRepository;
        this.foreignLanguageDTOMapper = foreignLanguageDTOMapper;
    }

    public List<ForeignLanguageDTO> getAllForeignLanguages() {
        List<ForeignLanguage> foreignLanguages = foreignLanguageRepository.findAll();
        return foreignLanguages.stream()
                .map(foreignLanguageDTOMapper)
                .collect(Collectors.toList());
    }

    public ForeignLanguageDTO createForeignLanguage(ForeignLanguageRequest foreignLanguageRequest) {
        ValidationUtils.validateSingleSpace(foreignLanguageRequest.getForeignLanguage());
        checkIfForeignLanguageAlreadyExists(foreignLanguageRequest.getForeignLanguage());

        ForeignLanguage foreignLanguage = new ForeignLanguage();
        foreignLanguage.setForeignLanguage(foreignLanguageRequest.getForeignLanguage());
        ForeignLanguage savedForeignLanguage = foreignLanguageRepository.save(foreignLanguage);
        return foreignLanguageDTOMapper.apply(savedForeignLanguage);
    }

    // For controller, because it returns DTO
    public ForeignLanguageDTO getForeignLanguageById(Integer foreignLanguageId) {
        checkIfForeignLanguageExistsOrThrow(foreignLanguageId);
        return foreignLanguageRepository.findById(foreignLanguageId).map(foreignLanguageDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public ForeignLanguage getForeignLanguage(Integer foreignLanguageId) {
        checkIfForeignLanguageExistsOrThrow(foreignLanguageId);
        return foreignLanguageRepository.findById(foreignLanguageId).orElse(null);
    }

    public ForeignLanguageDTO updateForeignLanguage(Integer foreignLanguageId, ForeignLanguageRequest foreignLanguageRequest) {
        ForeignLanguage existingForeignLanguage = getForeignLanguage(foreignLanguageId);
        boolean changes = false;

        if (foreignLanguageRequest.getForeignLanguage() != null && !foreignLanguageRequest.getForeignLanguage().equals(existingForeignLanguage.getForeignLanguage())) {
            ValidationUtils.validateSingleSpace(foreignLanguageRequest.getForeignLanguage());
            checkIfForeignLanguageAlreadyExistsWithCaseSensitivity(foreignLanguageRequest.getForeignLanguage(), existingForeignLanguage.getForeignLanguage());

            existingForeignLanguage.setForeignLanguage(foreignLanguageRequest.getForeignLanguage());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        ForeignLanguage updatedForeignLanguage = foreignLanguageRepository.save(existingForeignLanguage);
        return foreignLanguageDTOMapper.apply(updatedForeignLanguage);
    }

    public void deleteForeignLanguageById(Integer foreignLanguageId) {
        checkIfForeignLanguageExistsOrThrow(foreignLanguageId);
        foreignLanguageRepository.deleteById(foreignLanguageId);
    }

    private void checkIfForeignLanguageAlreadyExistsWithCaseSensitivity(String newLanguage, String currentLanguage) {
        ForeignLanguage foundLanguage = foreignLanguageRepository.findByForeignLanguageIgnoreCase(newLanguage);
        if (foundLanguage != null && !foundLanguage.getForeignLanguage().equalsIgnoreCase(currentLanguage)) {
            throw new ResourceAlreadyExistsException("The foreign language '" + newLanguage + "' already exists.");
        }
    }

    private void checkIfForeignLanguageExistsOrThrow(int foreignLanguageId) {
        if (!foreignLanguageRepository.existsById(foreignLanguageId)) {
            throw new ResourceNotFoundException("Foreign language not found.");
        }
    }

    private void checkIfForeignLanguageAlreadyExists(String name) {
        ForeignLanguage foreignLanguage = foreignLanguageRepository.findByForeignLanguage(name);
        if (foreignLanguage != null) {
            throw new ResourceAlreadyExistsException("The foreign language '" + name + "' already exists.");
        }
    }
}
