package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.StudentType;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.StudentTypeDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentTypeDTOMapper implements Function<StudentType, StudentTypeDTO> {
    @Override
    public StudentTypeDTO apply(StudentType studentType) {
        return new StudentTypeDTO(
                studentType.getId(),
                studentType.getType()
        );
    }
}
