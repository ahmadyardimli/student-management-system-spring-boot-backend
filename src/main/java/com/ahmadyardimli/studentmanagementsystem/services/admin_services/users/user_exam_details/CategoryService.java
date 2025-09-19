package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.CategoryDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Category;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.user_exam_details_mappers.CategoryDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details.CategoryRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.CategoryRequest;
import com.ahmadyardimli.studentmanagementsystem.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDTOMapper categoryDTOMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryDTOMapper categoryDTOMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryDTOMapper = categoryDTOMapper;
    }

    public List<CategoryDTO> getAllStudentCategories() {
        List<Category> studentCategories = categoryRepository.findAll();
        return studentCategories.stream()
                .map(categoryDTOMapper)
                .collect(Collectors.toList());
    }

    public CategoryDTO createStudentCategory(CategoryRequest categoryRequest) {
        ValidationUtils.validateSingleSpace(categoryRequest.getCategory());
        checkIfStudentCategoryAlreadyExists(categoryRequest.getCategory());

        Category studentCategory = new Category(
                categoryRequest.getCategory(),
                categoryRequest.getMinClass(),
                categoryRequest.getMaxClass()
        );

        Category savedStudentCategory = categoryRepository.save(studentCategory);
        return categoryDTOMapper.apply(savedStudentCategory);
    }

    // For controller, because it returns DTO
    public CategoryDTO getStudentCategoryById(Integer studentCategoryId) {
        checkIfStudentCategoryExistsOrThrow(studentCategoryId);
        return categoryRepository.findById(studentCategoryId).map(categoryDTOMapper).orElse(null);
    }

    // For services, because it returns Entity.
    public Category getStudentCategory(Integer studentCategoryId){
        checkIfStudentCategoryExistsOrThrow(studentCategoryId);
        return categoryRepository.findById(studentCategoryId).orElse(null);
    }

    public CategoryDTO updateStudentCategory(Integer studentCategoryId, CategoryRequest categoryRequest) {
        Category existingStudentCategory = getStudentCategory(studentCategoryId);
        boolean changes = false;

        if (categoryRequest.getCategory() != null && !categoryRequest.getCategory().equals(existingStudentCategory.getCategory())) {
            ValidationUtils.validateSingleSpace(categoryRequest.getCategory());
            checkIfStudentCategoryAlreadyExistsWithCaseSensitivity(categoryRequest.getCategory(), existingStudentCategory.getCategory());
            existingStudentCategory.setCategory(categoryRequest.getCategory());
            changes = true;
        }

        if (categoryRequest.getMinClass() != 0 && categoryRequest.getMinClass() != existingStudentCategory.getMinClass()) {
            existingStudentCategory.setMinClass(categoryRequest.getMinClass());
            changes = true;
        }

        if (categoryRequest.getMaxClass() != 0 && categoryRequest.getMaxClass() != existingStudentCategory.getMaxClass()) {
            existingStudentCategory.setMaxClass(categoryRequest.getMaxClass());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes were made.");
        }

        Category updatedStudentCategory = categoryRepository.save(existingStudentCategory);
        return categoryDTOMapper.apply(updatedStudentCategory);
    }

    private void checkIfStudentCategoryAlreadyExistsWithCaseSensitivity(String newCategory, String currentCategory) {
        Category foundStudentCategory = categoryRepository.findByCategoryIgnoreCase(newCategory);
        if (foundStudentCategory != null && !foundStudentCategory.getCategory().equalsIgnoreCase(currentCategory)) {
            throw new ResourceAlreadyExistsException("The category '" + newCategory + "' already exists.");
        }
    }

    public void deleteStudentCategoryById(Integer studentCategoryId) {
        checkIfStudentCategoryExistsOrThrow(studentCategoryId);
        categoryRepository.deleteById(studentCategoryId);
    }

    private void checkIfStudentCategoryExistsOrThrow(int studentCategoryId) {
        if (!categoryRepository.existsById(studentCategoryId)) {
            throw new ResourceNotFoundException("Category not found.");
        }
    }

    private void checkIfStudentCategoryAlreadyExists(String category) {
        Category studentCategory = categoryRepository.findByCategory(category);
        if (studentCategory != null) {
            throw new ResourceAlreadyExistsException("The category '" + category + "' already exists.");
        }
    }
}
