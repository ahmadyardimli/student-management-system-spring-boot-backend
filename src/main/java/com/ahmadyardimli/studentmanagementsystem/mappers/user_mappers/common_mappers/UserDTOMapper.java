package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        // Handle the UserType conversion
        if (user.getUserType() != null) {
            userDTO.setUserType(new UserTypeDTO(user.getUserType().getId(), user.getUserType().getType()));
        } else {
            userDTO.setUserType(null);
        }

        if (user.getUserStatus() != null) {
            userDTO.setStatus(new UserStatusDTO(user.getUserStatus().getId(), user.getUserStatus().getStatus()));
        } else {
            userDTO.setStatus(null);
        }

        userDTO.setCreatedAt(user.getCreatedAtFormatted());
        userDTO.setUpdatedAt(user.getUpdatedAtFormatted());

        // Set the password field
//        userDTO.setPasswordHash(user.getPasswordHash()); // Assuming passwordHash is stored in the User entity

        // Set other fields
//        userDTO.setUserTypeId(user.getUserType().getId());
        userDTO.setAuthKey(user.getAuthKey());
        userDTO.setVerificationToken(user.getVerificationToken());

        return userDTO;
    }
}
