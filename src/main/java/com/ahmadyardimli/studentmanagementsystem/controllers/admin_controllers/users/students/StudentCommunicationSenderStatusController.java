package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.students;

import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.students.StudentCommunicationSenderStatusDTO;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.students.StudentCommunicationSenderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin/users/students/student-communication-sender-statuses")
public class StudentCommunicationSenderStatusController {
    private final StudentCommunicationSenderStatusService studentCommunicationSenderStatusService;

    @Autowired
    public StudentCommunicationSenderStatusController(StudentCommunicationSenderStatusService studentCommunicationSenderStatusService) {
        this.studentCommunicationSenderStatusService = studentCommunicationSenderStatusService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllStudentCommunicationSenderStatuses() {
        List<StudentCommunicationSenderStatusDTO> studentCommunicationSenderStatuses = studentCommunicationSenderStatusService.getAllStatuses();
        try {
            if (!studentCommunicationSenderStatuses.isEmpty())
                return ResponseEntity.ok(studentCommunicationSenderStatuses);
            else
                return new ResponseEntity<>("No student communication sender statuses found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching student communication sender statuses.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
