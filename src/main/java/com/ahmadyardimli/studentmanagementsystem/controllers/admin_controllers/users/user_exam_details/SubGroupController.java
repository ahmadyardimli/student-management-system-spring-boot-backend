package com.ahmadyardimli.studentmanagementsystem.controllers.admin_controllers.users.user_exam_details;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.user_exam_details.SubGroupDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.SubGroup;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.user_exam_details.SubGroupRequest;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.SubGroupService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/user_exam_details/sub-groups")
public class SubGroupController {
    private final SubGroupService subGroupService;

    @Autowired
    public SubGroupController(SubGroupService subGroupService) {
        this.subGroupService = subGroupService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSubGroups() {
        List<SubGroupDTO> subGroups = subGroupService.getAllSubGroups();
        try {
            if (!subGroups.isEmpty())
                return ResponseEntity.ok(subGroups);
            else
                return new ResponseEntity<>("No sub-groups found.", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("No sub-groups found.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-sub-group")
    public ResponseEntity<Object> createSubGroup(@RequestBody SubGroupRequest subGroupRequest) {
        try {
            SubGroupDTO newSubGroup = subGroupService.createSubGroup(subGroupRequest);
            if (newSubGroup != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newSubGroup);
            else
                return new ResponseEntity<>("Failed to create sub-group.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update-sub-group/{subGroupId}")
    public ResponseEntity<Object> updateSubGroup(
            @PathVariable Integer subGroupId,
            @RequestBody SubGroupRequest subGroupRequest) {
        try {
            SubGroupDTO updatedSubGroup = subGroupService.updateSubGroup(subGroupId, subGroupRequest);
            if (updatedSubGroup != null)
                return new ResponseEntity<>(updatedSubGroup, HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to update sub-group.", HttpStatus.BAD_REQUEST);
        } catch (RequestValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{subGroupId}")
    public ResponseEntity<Object> getSubGroup(@PathVariable Integer subGroupId) {
        try {
            SubGroupDTO subGroup = subGroupService.getSubGroupById(subGroupId);
            if (subGroup != null)
                return ResponseEntity.ok(subGroup);
            else return new ResponseEntity<>("Sub-group not found.", HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while fetching the sub-group.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{subGroupId}")
    public ResponseEntity<Object> deleteSubGroup(@PathVariable Integer subGroupId) {
        try {
            SubGroup subGroup = subGroupService.getSubGroup(subGroupId);

            if (subGroup != null){
                List<Student> students = subGroup.getStudents();

                for (Student student : students)
                    student.setSubGroup(null);

            }

            subGroupService.deleteSubGroupById(subGroupId);
            return new ResponseEntity<>("Sub-group deleted successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the sub-group.", HttpStatus.BAD_REQUEST);
        }
    }
}
