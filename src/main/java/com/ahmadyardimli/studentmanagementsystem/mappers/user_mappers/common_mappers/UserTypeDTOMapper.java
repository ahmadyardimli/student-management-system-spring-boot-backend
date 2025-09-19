package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserType;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserTypeDTOMapper implements Function<UserType, UserTypeDTO> {
    @Override
    public UserTypeDTO apply(UserType userType) {
        return new UserTypeDTO(
                userType.getId(),
                userType.getType()
        );
    }
}
