package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.teacher_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.TeacherCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherCommunicationSenderStatusDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TeacherCommunicationSenderStatusDTOMapper implements Function<TeacherCommunicationSenderStatus, TeacherCommunicationSenderStatusDTO> {
    @Override
    public TeacherCommunicationSenderStatusDTO apply(TeacherCommunicationSenderStatus teacherCommunicationSenderStatus) {
        return new TeacherCommunicationSenderStatusDTO(
                teacherCommunicationSenderStatus.getId(),
                teacherCommunicationSenderStatus.getStatus()
        );
    }
}
