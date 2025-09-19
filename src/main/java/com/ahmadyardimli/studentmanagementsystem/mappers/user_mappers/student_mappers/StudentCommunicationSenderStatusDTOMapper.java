package com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.student_mappers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.StudentCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentCommunicationSenderStatusDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentCommunicationSenderStatusDTOMapper implements Function<StudentCommunicationSenderStatus, StudentCommunicationSenderStatusDTO> {
    @Override
    public StudentCommunicationSenderStatusDTO apply(StudentCommunicationSenderStatus studentCommunicationSenderStatus) {
        return new StudentCommunicationSenderStatusDTO(
                studentCommunicationSenderStatus.getId(),
                studentCommunicationSenderStatus.getStatus()
        );
    }
}
