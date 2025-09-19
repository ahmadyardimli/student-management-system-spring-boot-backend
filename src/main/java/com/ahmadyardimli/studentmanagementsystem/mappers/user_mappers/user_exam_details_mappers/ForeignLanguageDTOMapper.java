package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ForeignLanguage;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.ForeignLanguageDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ForeignLanguageDTOMapper implements Function<ForeignLanguage, ForeignLanguageDTO> {
    @Override
    public ForeignLanguageDTO apply(ForeignLanguage foreignLanguage) {
        return new ForeignLanguageDTO(
                foreignLanguage.getId(),
                foreignLanguage.getForeignLanguage()
        );
    }
}
