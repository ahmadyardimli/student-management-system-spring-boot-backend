package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.common_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserStatus;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserStatusDTOMapper implements Function<UserStatus, UserStatusDTO> {
    @Override
    public UserStatusDTO apply(UserStatus userStatus) {
        return new UserStatusDTO(
                userStatus.getId(),
                userStatus.getStatus()
        );
    }
}
