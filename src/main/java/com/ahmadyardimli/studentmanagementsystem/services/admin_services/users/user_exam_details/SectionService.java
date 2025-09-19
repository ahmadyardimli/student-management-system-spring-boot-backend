package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SectionDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Section;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.SectionDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.SectionRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SectionRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SectionDTOMapper sectionDTOMapper;

    @Autowired
    public SectionService(SectionRepository sectionRepository, SectionDTOMapper sectionDTOMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionDTOMapper = sectionDTOMapper;
    }

    public List<SectionDTO> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream()
                .map(sectionDTOMapper)
                .collect(Collectors.toList());
    }

    public SectionDTO createSection(SectionRequest sectionRequest) {
        ValidationUtils.validateSingleSpace(sectionRequest.getSection());
        checkIfSectionAlreadyExists(sectionRequest.getSection());

        Section section = new Section(sectionRequest.getSection());
        Section savedSection = sectionRepository.save(section);
        return sectionDTOMapper.apply(savedSection);
    }

    // For controller, because it returns DTO
    public SectionDTO getSectionById(Integer sectionId) {
        checkIfSectionExistsOrThrow(sectionId);
        return sectionRepository.findById(sectionId).map(sectionDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public Section getSection(Integer sectionId) {
        checkIfSectionExistsOrThrow(sectionId);
        return sectionRepository.findById(sectionId).orElse(null);
    }

    public SectionDTO updateSection(Integer sectionId, SectionRequest sectionRequest) {
        Section existingSection = getSection(sectionId);
        boolean changes = false;

        if (sectionRequest.getSection() != null && !sectionRequest.getSection().equals(existingSection.getSection())) {
            ValidationUtils.validateSingleSpace(sectionRequest.getSection());
            checkIfSectionAlreadyExistsWithCaseSensitivity(sectionRequest.getSection(), existingSection.getSection());

            existingSection.setSection(sectionRequest.getSection());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        Section updatedSection = sectionRepository.save(existingSection);
        return sectionDTOMapper.apply(updatedSection);
    }

    public void deleteSectionById(Integer sectionId) {
        checkIfSectionExistsOrThrow(sectionId);
        sectionRepository.deleteById(sectionId);
    }

    private void checkIfSectionAlreadyExistsWithCaseSensitivity(String newSection, String currentSection) {
        Section foundSection = sectionRepository.findBySectionIgnoreCase(newSection);
        if (foundSection != null && !foundSection.getSection().equalsIgnoreCase(currentSection)) {
            throw new ResourceAlreadyExistsException("The section '" + newSection + "' already exists.");
        }
    }

    private void checkIfSectionExistsOrThrow(int sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new ResourceNotFoundException("Section not found.");
        }
    }

    private void checkIfSectionAlreadyExists(String section) {
        Section studentSection = sectionRepository.findBySection(section);
        if (studentSection != null) {
            throw new ResourceAlreadyExistsException("The section '" + section + "' already exists.");
        }
    }
}
