package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserType;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers.UserTypeDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserTypeRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.commons.UserTypeRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTypeService {
    private final UserTypeRepository userTypeRepository;
    private final UserTypeDTOMapper userTypeDTOMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository, UserTypeDTOMapper userTypeDTOMapper) {
        this.userTypeRepository = userTypeRepository;
        this.userTypeDTOMapper = userTypeDTOMapper;
    }

    public List<UserTypeDTO> getAllUserTypes() {
        List<UserType> userTypes = userTypeRepository.findAll();
        return userTypes.stream()
                .map(userTypeDTOMapper)
                .collect(Collectors.toList());
    }

    public UserTypeDTO saveUserType(UserTypeRequest userTypeRequest) {
        ValidationUtils.validateSingleSpace(userTypeRequest.getType());
        checkIfUserTypeAlreadyExists(userTypeRequest.getType());

        UserType userType = new UserType(userTypeRequest.getType());
        UserType savedUserType = userTypeRepository.save(userType);
        return userTypeDTOMapper.apply(savedUserType);
    }

    // for controller, because it returns DTO
    public UserTypeDTO getUserTypeById(Integer userTypeId) {
        checkIfUserTypeExistsOrThrow(userTypeId);
        return userTypeRepository.findById(userTypeId).map(userTypeDTOMapper).orElse(null);
    }

    // for other services,to get teacher type
    public UserType getUserType(Integer userTypeId){
        checkIfUserTypeExistsOrThrow(userTypeId);
        return userTypeRepository.findById(userTypeId).orElse(null);
    }

    public UserTypeDTO updateUserType(Integer userTypeId, UserTypeRequest userTypeRequest) {
        UserType existingUserType = getUserType(userTypeId);
        boolean changes = false;
        String newType = userTypeRequest.getType();

        // Check if the new type is provided and different from the current type
        if (newType != null && !newType.equals(existingUserType.getType())) {
            // Validate the new type's format
            ValidationUtils.validateSingleSpace(newType);
            // Check if the new type already exists (case-insensitive), excluding the current type
            checkIfUserTypeAlreadyExistsWithCaseSensitivity(newType, existingUserType.getType());
            // Update the type
            existingUserType.setType(newType);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        UserType updatedUserType = userTypeRepository.save(existingUserType);

        return userTypeDTOMapper.apply(updatedUserType);
    }

    public void deleteUserTypeById(int userTypeId) {
        checkIfUserTypeExistsOrThrow(userTypeId);
        // check if any users are associated with this userType
        boolean usersExist = userRepository.existsByUserType_Id(userTypeId);
        if (usersExist) {
            throw new RequestValidationException("This user type cannot be deleted because it is assigned to existing users.");
        }
        // delete if no users are associated
        userTypeRepository.deleteById(userTypeId);
    }

    private void checkIfUserTypeAlreadyExistsWithCaseSensitivity(String newType, String currentType) {
        UserType foundUserType = userTypeRepository.findByTypeIgnoreCase(newType);
        // If another user type exists with the same name (ignoring case) and it's not the current type, throw an exception
        if (foundUserType != null && !foundUserType.getType().equalsIgnoreCase(currentType)) {
            throw new ResourceAlreadyExistsException("The user type '" + newType + "' already exists.");
        }
    }

    private void checkIfUserTypeExistsOrThrow(int userTypeId) {
        if (!userTypeRepository.existsById(userTypeId)) {
            throw new ResourceNotFoundException("UserType with ID " + userTypeId + " not found");
        }
    }

    private void checkIfUserTypeAlreadyExists(String name) {
        UserType userType = userTypeRepository.findByType(name);
        if (userType != null) {
            throw new ResourceAlreadyExistsException("The user type '" + name + "' already exists.");
        }
    }
}
