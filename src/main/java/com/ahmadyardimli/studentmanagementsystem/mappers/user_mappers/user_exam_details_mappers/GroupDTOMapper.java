package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Group;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.GroupDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GroupDTOMapper implements Function<Group, GroupDTO> {
    @Override
    public GroupDTO apply(Group group) {
        return new GroupDTO(
                group.getId(),
                group.getGroup()
        );
    }
}
