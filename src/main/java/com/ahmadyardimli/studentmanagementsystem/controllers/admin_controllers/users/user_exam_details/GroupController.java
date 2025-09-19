package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.GroupDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Group;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.GroupRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.GroupService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllStudentGroups() {
        List<GroupDTO> studentGroups = groupService.getAllGroups();
        try {
            if (!studentGroups.isEmpty())
                return ResponseEntity.ok(studentGroups);
            else
                return new ResponseEntity<>("No groups found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching groups.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-group")
    public ResponseEntity<Object> createGroup(@RequestBody GroupRequest groupRequest) {
        try {
            GroupDTO newGroup = groupService.createGroup(groupRequest);
            if (newGroup != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
            else
                return new ResponseEntity<>("Failed to create group.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-group/{groupId}")
    public ResponseEntity<Object> updateGroup(
            @PathVariable Integer groupId,
            @RequestBody GroupRequest groupRequest) {
        try {
            GroupDTO updatedGroup = groupService.updateGroup(groupId, groupRequest);
            if (updatedGroup != null)
                return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update group.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Object> getGroup(@PathVariable Integer groupId) {
        try {
            GroupDTO studentGroup = groupService.getGroupById(groupId);
            if (studentGroup != null)
                return ResponseEntity.ok(studentGroup);
            else return new ResponseEntity<>("Group not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the group.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Integer groupId) {
        try {
            Group group = groupService.getGroup(groupId);

            if (group != null) {
                List<Student> students = group.getStudents();

                for (Student student : students)
                    student.setGroup(null);
            }

            groupService.deleteGroupById(groupId);
            return new ResponseEntity<>("Group deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the group.", HttpStatus.BAD_REQUEST);
        }
    }
}