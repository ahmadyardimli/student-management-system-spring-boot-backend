package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Category;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryDTOMapper implements Function<Category, CategoryDTO> {
    @Override
    public CategoryDTO apply(Category studentCategory) {
        return new CategoryDTO(
                studentCategory.getId(),
                studentCategory.getCategory(),
                studentCategory.getMinClass(),
                studentCategory.getMaxClass()
        );
    }
}
