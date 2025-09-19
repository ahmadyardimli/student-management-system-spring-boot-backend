package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.student_mappers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.*;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentDTOMapper implements Function<Student, StudentDTO> {
    @Override
    public StudentDTO apply(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());

        // User-related information if available
        if (student.getUser() != null) {
            User user = student.getUser();
            studentDTO.setUserId(user.getId());

            // Map User entity to UserDTO
            UserDTO userDTO = mapUserToDTO(user);
            studentDTO.setUser(userDTO);
        }

        // Student-specific information
        studentDTO.setName(student.getName());
        studentDTO.setSurname(student.getSurname());
        studentDTO.setStudentCode(student.getStudentCode());
        studentDTO.setFatherName(student.getFatherName());
        studentDTO.setMobilePhone(student.getMobilePhone());
        studentDTO.setSchoolClassCode(student.getSchoolClassCode());
        studentDTO.setAddress(student.getAddress());

        // Mapping student class
        if(student.getClassNumber() != null){
            ClassNumberDTO classNumberDTO = new ClassNumberDTO(student.getClassNumber().getId(), student.getClassNumber().getNumberValue());
            studentDTO.setClassNumber(classNumberDTO);
        }

        // Mapping class letter
        if (student.getClassLetter() != null) {
            ClassLetterDTO classLetterDTO = new ClassLetterDTO(
                    student.getClassLetter().getId(),
                    student.getClassLetter().getLetterValue()
            );
            studentDTO.setClassLetter(classLetterDTO);
        }

        // Mapping student type
        if (student.getStudentType() != null) {
            StudentTypeDTO studentTypeDTO = new StudentTypeDTO(student.getStudentType().getId(), student.getStudentType().getType());
            studentDTO.setStudentType(studentTypeDTO);
        }

        // Mapping group
        if (student.getGroup() != null) {
            GroupDTO groupDTO = new GroupDTO(student.getGroup().getId(), student.getGroup().getGroup());
            studentDTO.setGroup(groupDTO);
        }

        // Mapping sub group
        if (student.getSubGroup() != null) {
            SubGroupDTO subGroupDTO = new SubGroupDTO(student.getSubGroup().getId(), student.getSubGroup().getSubGroup());
            studentDTO.setSubGroup(subGroupDTO);
        }

        // mapping category
        if (student.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO(student.getCategory().getId(),
                    student.getCategory().getCategory(),
                    student.getCategory().getMinClass(),
                    student.getCategory().getMaxClass());
            studentDTO.setCategory(categoryDTO);
        }

        // mapping section
        if (student.getSection() != null) {
            SectionDTO sectionDTO = new SectionDTO(student.getSection().getId(), student.getSection().getSection());
            studentDTO.setSection(sectionDTO);
        }

        // mapping foreign language
        if (student.getForeignLanguage() != null) {
            ForeignLanguageDTO foreignLanguageDTO = new ForeignLanguageDTO(student.getForeignLanguage().getId(),
                    student.getForeignLanguage().getForeignLanguage());
            studentDTO.setForeignLanguage(foreignLanguageDTO);
        }

        if (student.getCommunicationSenderStatus() != null) {
            StudentCommunicationSenderStatusDTO studentCommunicationSenderStatusDTO =
                    new StudentCommunicationSenderStatusDTO(student.getCommunicationSenderStatus().getId()
                            , student.getCommunicationSenderStatus().getStatus());
            studentDTO.setCommunicationSenderStatus(studentCommunicationSenderStatusDTO);
        }

        return studentDTO;
    }

    // Method to map User entity to UserDTO
    private UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAtFormatted());
        userDTO.setUpdatedAt(user.getUpdatedAtFormatted());

        // Map UserTypeDTO if available
        if (user.getUserType() != null) {
            UserTypeDTO userTypeDTO = new UserTypeDTO(user.getUserType().getId(), user.getUserType().getType());
            userDTO.setUserType(userTypeDTO);
        }

        // Map UserStatusDTO if available
        if (user.getUserStatus() != null) {
            UserStatusDTO userStatusDTO = new UserStatusDTO(user.getUserStatus().getId(), user.getUserStatus().getStatus());
            userDTO.setStatus(userStatusDTO);
        }

        return userDTO;
    }
}
