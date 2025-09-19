package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.CategoryDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Category;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.CategoryRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.CategoryService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllCategories() {
        List<CategoryDTO> studentCategories = categoryService.getAllStudentCategories();
        try {
            if (!studentCategories.isEmpty())
                return ResponseEntity.ok(studentCategories);
            else
                return new ResponseEntity<>("No student categories found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching student categories.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-category")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryDTO newStudentCategory = categoryService.createStudentCategory(categoryRequest);
            if (newStudentCategory != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newStudentCategory);
            else
                return new ResponseEntity<>("Failed to create student category.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-category/{categoryId}")
    public ResponseEntity<Object> updateCategory(
            @PathVariable Integer studentCategoryId,
            @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryDTO updatedStudentCategory = categoryService.updateStudentCategory(studentCategoryId, categoryRequest);
            if (updatedStudentCategory != null)
                return new ResponseEntity<>(updatedStudentCategory, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update student category.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{studentCategoryId}")
    public ResponseEntity<Object> getCategory(@PathVariable Integer studentCategoryId) {
        try {
            CategoryDTO studentCategory = categoryService.getStudentCategoryById(studentCategoryId);
            if (studentCategory != null)
                return ResponseEntity.ok(studentCategory);
            else return new ResponseEntity<>("Student category not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the student category.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Integer studentCategoryId) {
        try {
            Category studentCategory = categoryService.getStudentCategory(studentCategoryId);

            if (studentCategory != null){
                List<Student> students = studentCategory.getStudents();

                for (Student student : students)
                    student.setCategory(null);
            }

            categoryService.deleteStudentCategoryById(studentCategoryId);
            return new ResponseEntity<>("Student category deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the student category.", HttpStatus.BAD_REQUEST);
        }
    }
}