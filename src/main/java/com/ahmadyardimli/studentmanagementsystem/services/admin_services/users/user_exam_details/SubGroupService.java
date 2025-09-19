package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SubGroupDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.SubGroup;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.SubGroupDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.SubGroupRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SubGroupRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubGroupService {
    private final SubGroupRepository subGroupRepository;
    private final SubGroupDTOMapper subGroupDTOMapper;

    @Autowired
    public SubGroupService(SubGroupRepository subGroupRepository, SubGroupDTOMapper subGroupDTOMapper) {
        this.subGroupRepository = subGroupRepository;
        this.subGroupDTOMapper = subGroupDTOMapper;
    }

    public List<SubGroupDTO> getAllSubGroups() {
        List<SubGroup> subGroups = subGroupRepository.findAll();
        return subGroups.stream()
                .map(subGroupDTOMapper)
                .collect(Collectors.toList());
    }

    public SubGroupDTO createSubGroup(SubGroupRequest subGroupRequest) {
        ValidationUtils.validateSingleSpace(subGroupRequest.getSubGroup());
        // Check if the alternative group with the given name already exists
        checkIfSubGroupAlreadyExists(subGroupRequest.getSubGroup());

        SubGroup subGroup = new SubGroup(subGroupRequest.getSubGroup());
        SubGroup savedSubGroup = subGroupRepository.save(subGroup);
        return subGroupDTOMapper.apply(savedSubGroup);
    }

    // For controller, because it returns DTO
    public SubGroupDTO getSubGroupById(Integer subGroupId) {
        checkIfSubGroupExistsOrThrow(subGroupId);
        return subGroupRepository.findById(subGroupId).map(subGroupDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public SubGroup getSubGroup(Integer subGroupId){
        checkIfSubGroupExistsOrThrow(subGroupId);
        return subGroupRepository.findById(subGroupId).orElse(null);
    }

    public SubGroupDTO updateSubGroup(Integer subGroupId, SubGroupRequest subGroupRequest) {
        SubGroup existingSubGroup = getSubGroup(subGroupId);
        boolean changes = false;

        if (subGroupRequest.getSubGroup() != null && !subGroupRequest.getSubGroup().equals(existingSubGroup.getSubGroup())) {
            ValidationUtils.validateSingleSpace(subGroupRequest.getSubGroup());
            checkIfSubGroupAlreadyExistsWithCaseSensitivity(subGroupRequest.getSubGroup(), existingSubGroup.getSubGroup());

            existingSubGroup.setSubGroup(subGroupRequest.getSubGroup());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        SubGroup updatedSubGroup = subGroupRepository.save(existingSubGroup);
        return subGroupDTOMapper.apply(updatedSubGroup);
    }

    public void deleteSubGroupById(Integer subGroupId) {
        checkIfSubGroupExistsOrThrow(subGroupId);
        subGroupRepository.deleteById(subGroupId);
    }

    private void checkIfSubGroupExistsOrThrow(int subGroupId) {
        if (!subGroupRepository.existsById(subGroupId)) {
            throw new ResourceNotFoundException("Sub group not found.");
        }
    }

    private void checkIfSubGroupAlreadyExistsWithCaseSensitivity(String newSubGroup, String currentSubGroup) {
        SubGroup foundSubGroup = subGroupRepository.findBySubGroupIgnoreCase(newSubGroup);
        if (foundSubGroup != null && !foundSubGroup.getSubGroup().equalsIgnoreCase(currentSubGroup)) {
            throw new ResourceAlreadyExistsException("The subgroup '" + newSubGroup + "' already exists.");
        }
    }

    private void checkIfSubGroupAlreadyExists(String name) {
        SubGroup subGroup = subGroupRepository.findBySubGroup(name);
        if (subGroup != null) {
            throw new ResourceAlreadyExistsException("The subgroup '" + name + "' already exists.");
        }
    }
}
