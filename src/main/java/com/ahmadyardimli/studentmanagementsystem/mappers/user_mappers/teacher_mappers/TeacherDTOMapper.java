package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.teacher_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.Teacher;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.commons.UserTypeDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TeacherDTOMapper implements Function<Teacher, TeacherDTO> {
    @Override
    public TeacherDTO apply(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());

        // User id
        User user = teacher.getUser();
        teacherDTO.setUserId(user.getId());
        if (user != null) {
            UserDTO userDTO = mapUserToDTO(user);
            teacherDTO.setUser(userDTO);
        }

        // Teacher-specific information
        teacherDTO.setName(teacher.getName());
        teacherDTO.setSurname(teacher.getSurname());
        // Set subject IDs in the DTO
        List<Integer> subjectIds = teacher.getSubjects().stream()
                .map(Subject::getId)
                .collect(Collectors.toList());
        teacherDTO.setSubject_ids(subjectIds);

        // Mapping communicationSenderStatus
        if (teacher.getCommunicationSenderStatus() != null) {
            TeacherCommunicationSenderStatusDTO teacherCommunicationSenderStatusDTO = new TeacherCommunicationSenderStatusDTO();
            teacherCommunicationSenderStatusDTO.setId(teacher.getCommunicationSenderStatus().getId());
            teacherCommunicationSenderStatusDTO.setStatus(teacher.getCommunicationSenderStatus().getStatus());
            teacherDTO.setCommunicationSenderStatus(teacherCommunicationSenderStatusDTO);
        }
        return teacherDTO;
    }

    private UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAtFormatted());
        userDTO.setUpdatedAt(user.getUpdatedAtFormatted());

        // Map UserTypeDTO
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        if (user.getUserType() != null){
            userTypeDTO.setId(user.getUserType().getId());
            userTypeDTO.setType(user.getUserType().getType());
            userDTO.setUserType(userTypeDTO);
        }

        // Map UserStatusDTO
        if (user.getUserStatus() != null){
            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setId(user.getUserStatus().getId());
            userStatusDTO.setStatus(user.getUserStatus().getStatus());
            userDTO.setStatus(userStatusDTO);
        }

        return userDTO;
    }
}
