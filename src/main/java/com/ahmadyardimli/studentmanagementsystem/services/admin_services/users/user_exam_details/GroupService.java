package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.GroupDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Group;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.GroupDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.GroupRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.GroupRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupDTOMapper groupDTOMapper;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupDTOMapper groupDTOMapper) {
        this.groupRepository = groupRepository;
        this.groupDTOMapper = groupDTOMapper;
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .map(groupDTOMapper)
                .collect(Collectors.toList());
    }

    public GroupDTO createGroup(GroupRequest groupRequest) {
        ValidationUtils.validateSingleSpace(groupRequest.getGroup());
        // Check if the student group with the given name already exists
        checkIfGroupAlreadyExists(groupRequest.getGroup());

        Group group = new Group(groupRequest.getGroup());
        Group savedGroup = groupRepository.save(group);
        return groupDTOMapper.apply(savedGroup);
    }

    // For controller, because it returns DTO
    public GroupDTO getGroupById(Integer groupId) {
        checkIfGroupExistsOrThrow(groupId);
        return groupRepository.findById(groupId).map(groupDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public Group getGroup(Integer groupId){
        checkIfGroupExistsOrThrow(groupId);
        return groupRepository.findById(groupId).orElse(null);
    }

    public GroupDTO updateGroup(Integer groupId, GroupRequest groupRequest) {
        Group existingGroup = getGroup(groupId);
        boolean changes = false;

        if (groupRequest.getGroup() != null && !groupRequest.getGroup().equals(existingGroup.getGroup())) {
            ValidationUtils.validateSingleSpace(groupRequest.getGroup());
            checkIfGroupAlreadyExistsWithCaseSensitivity(groupRequest.getGroup(), existingGroup.getGroup());
            existingGroup.setGroup(groupRequest.getGroup());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        Group updatedGroup = groupRepository.save(existingGroup);
        return groupDTOMapper.apply(updatedGroup);
    }

    public void deleteGroupById(Integer groupId) {
        checkIfGroupExistsOrThrow(groupId);
        groupRepository.deleteById(groupId);
    }

    private void checkIfGroupAlreadyExistsWithCaseSensitivity(String newGroup, String currentGroup) {
        Group foundGroup = groupRepository.findByGroupIgnoreCase(newGroup);
        if (foundGroup != null && !foundGroup.getGroup().equalsIgnoreCase(currentGroup)) {
            throw new ResourceAlreadyExistsException("The group '" + newGroup + "' already exists.");
        }
    }

    private void checkIfGroupExistsOrThrow(int groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group not found.");
        }
    }

    private void checkIfGroupAlreadyExists(String name) {
        Group group = groupRepository.findByGroup(name);
        if (group != null) {
            throw new ResourceAlreadyExistsException("The group '" + name + "' already exists.");
        }
    }
}
