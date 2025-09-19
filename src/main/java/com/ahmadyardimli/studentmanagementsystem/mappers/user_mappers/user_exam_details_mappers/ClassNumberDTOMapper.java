package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassNumber;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ClassNumberDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClassNumberDTOMapper implements Function<ClassNumber, ClassNumberDTO> {
    @Override
    public ClassNumberDTO apply(ClassNumber classNumber) {
        return new ClassNumberDTO(
                classNumber.getId(),
                classNumber.getNumberValue()
        );
    }
}
