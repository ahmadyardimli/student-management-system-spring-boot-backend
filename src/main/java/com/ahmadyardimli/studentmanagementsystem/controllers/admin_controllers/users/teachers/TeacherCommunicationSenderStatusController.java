package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.teachers;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers.TeacherCommunicationSenderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin/users/teachers/teacher-communication-sender-statuses")
public class TeacherCommunicationSenderStatusController {
    private final TeacherCommunicationSenderStatusService teacherCommunicationSenderStatusService;

    @Autowired
    public TeacherCommunicationSenderStatusController(TeacherCommunicationSenderStatusService teacherCommunicationSenderStatusService) {
        this.teacherCommunicationSenderStatusService = teacherCommunicationSenderStatusService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTeacherCommunicationSenderStatuses() {
        List<TeacherCommunicationSenderStatusDTO> teacherCommunicationSenderStatuses = teacherCommunicationSenderStatusService.getAllStatuses();
        try {
            if (!teacherCommunicationSenderStatuses.isEmpty())
                return ResponseEntity.ok(teacherCommunicationSenderStatuses);
            else
                return new ResponseEntity<>("No teacher communication sender statuses found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching teacher communication sender statuses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
