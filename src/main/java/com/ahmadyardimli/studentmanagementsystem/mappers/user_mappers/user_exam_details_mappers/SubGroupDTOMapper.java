package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.SubGroup;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SubGroupDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SubGroupDTOMapper implements Function<SubGroup, SubGroupDTO> {
    @Override
    public SubGroupDTO apply(SubGroup subGroup) {
        return new SubGroupDTO(
                subGroup.getId(),
                subGroup.getSubGroup()
        );
    }
}
