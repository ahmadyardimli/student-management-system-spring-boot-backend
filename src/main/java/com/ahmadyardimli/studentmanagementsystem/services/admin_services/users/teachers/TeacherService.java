package com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.teachers;

import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.user_exam_details.SubjectService;
import com.ahmadyardimli.studentmanagementsystem.dtos.user_dtos.teachers.TeacherDTO;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.TeacherCommunicationSenderStatus;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.Teacher;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RequestValidationException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.ahmadyardimli.studentmanagementsystem.mappers.user_mappers.teacher_mappers.TeacherDTOMapper;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers.TeacherRepository;
import com.ahmadyardimli.studentmanagementsystem.requests.user_requests.teachers.TeacherRequest;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.utils.UserServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherDTOMapper teacherDTOMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TeacherCommunicationSenderStatusService teacherCommunicationSenderStatusService;
    private final UserServiceUtils userServiceUtils;
    private final SubjectService subjectService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, TeacherDTOMapper teacherDTOMapper, UserRepository userRepository, UserService userService, TeacherCommunicationSenderStatusService teacherCommunicationSenderStatusService, UserServiceUtils userServiceUtils, SubjectService subjectService) {
        this.teacherRepository = teacherRepository;
        this.teacherDTOMapper = teacherDTOMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.teacherCommunicationSenderStatusService = teacherCommunicationSenderStatusService;
        this.userServiceUtils = userServiceUtils;
        this.subjectService = subjectService;
    }

    public TeacherDTO createTeacher(TeacherRequest teacherRequest) {
        int userId = teacherRequest.getUserId();
        List<Integer> subject_ids = teacherRequest.getSubject_ids();

        // Check if the user ID exists in either Teacher or Student
        userServiceUtils.checkIfUserIdExistsInStudentOrTeacherOrThrow(userId, "teacher");

        User user = userService.getUser(userId);
        Teacher teacher = new Teacher();

        List<Subject> subjects = subject_ids.stream()
                .map(subjectService::getSubject)
                .collect(Collectors.toList());

        TeacherCommunicationSenderStatus teacherCommunicationSenderStatus = teacherCommunicationSenderStatusService.getCommunicationSenderStatus(teacherRequest.getCommunicationSenderStatusId());

        teacher.setName(teacherRequest.getName());
        teacher.setSurname(teacherRequest.getSurname());
        teacher.setSubjects(subjects);

        if (user.getUserType().getType().equalsIgnoreCase("Teacher")){
            teacher.setUser(user);
        }
        else {
            throw new RequestValidationException("User type must be \"Teacher\".");
        }

        teacher.setCommunicationSenderStatus(teacherCommunicationSenderStatus);

        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherDTOMapper.apply(savedTeacher);
    }

    public ResponseEntity<Object> getAllTeachers() {
        List<TeacherDTO> teachers = teacherRepository.findAll().stream()
                .map(teacherDTOMapper)
                .collect(Collectors.toList());

        if (!teachers.isEmpty()) {
            for (TeacherDTO teacher : teachers) {
                setTeacherSubjects(teacher);
            }
            return ResponseEntity.ok(teachers);
        } else {
            return new ResponseEntity<>("No teachers found.", HttpStatus.NOT_FOUND);
        }
    }

    // for controller, because it returns DTO
    public TeacherDTO getTeacherById(int teacherId) {
        checkIfTeacherExistsOrThrow(teacherId);
        TeacherDTO teacher = teacherRepository.findById(teacherId)
                .map(teacherDTOMapper)
                .orElse(null);
        if (teacher != null) {
            setTeacherSubjects(teacher);
        }
        return teacher;
    }

    // for services to get teacher
    public Teacher getTeacher(int teacherId) {
        checkIfTeacherExistsOrThrow(teacherId);
        return teacherRepository.findById(teacherId).orElse(null);
    }

    public TeacherDTO updateTeacher(int teacherId, TeacherRequest teacherRequest) {
        Teacher existingTeacher = getTeacher(teacherId);

            boolean changes = false;

            if (teacherRequest.getName() != null && !teacherRequest.getName().equals(existingTeacher.getName())) {
                existingTeacher.setName(teacherRequest.getName());
                changes = true;
            }

            if (teacherRequest.getSurname() != null && !teacherRequest.getSurname().equals(existingTeacher.getSurname())) {
                existingTeacher.setSurname(teacherRequest.getSurname());
                changes = true;
            }

            if (teacherRequest.getSubject_ids() != null){
                List<Subject> newSubjects = teacherRequest.getSubject_ids().stream()
                        .map(subjectService::getSubject) // Get Subject entities by ID
                        .collect(Collectors.toList());

                List<Integer> newSubject_ids = teacherRequest.getSubject_ids();

                List<Integer> existingSubject_ids = existingTeacher.getSubjects()
                        .stream()
                        .map(Subject::getId) // Assuming Subject has getId method
                        .collect(Collectors.toList());

                boolean areListsEqual = newSubject_ids.equals(existingSubject_ids);

                if (!areListsEqual) {
                    existingTeacher.setSubjects(newSubjects);
                    changes = true;
                }
            }

            if (teacherRequest.getUserId() != 0 && teacherRequest.getUserId() != existingTeacher.getUser().getId()) {
                User user = userService.getUser(teacherRequest.getUserId());
                // Check if the user ID exists in either Teacher or Student
                userServiceUtils.checkIfUserIdExistsInStudentOrTeacherOrThrow(teacherRequest.getUserId(), "teacher");

                // Update the associated user with the new user_id
                existingTeacher.setUser(user);
                changes = true;
            }

            if (teacherRequest.getCommunicationSenderStatusId() != 0 && teacherRequest.getCommunicationSenderStatusId() != existingTeacher.getCommunicationSenderStatus().getId()) {
                TeacherCommunicationSenderStatus teacherCommunicationSenderStatus = teacherCommunicationSenderStatusService.getCommunicationSenderStatus(teacherRequest.getCommunicationSenderStatusId());
                existingTeacher.setCommunicationSenderStatus(teacherCommunicationSenderStatus);
                changes = true;
             }

            if (!changes) {
                throw new RequestValidationException("No changes were made.");
            }

            // if changes made update the updated_at record of users
            User userToUpdate = existingTeacher.getUser();
            userToUpdate.setUpdatedAt(LocalDateTime.now());
            userRepository.save(userToUpdate);


            Teacher updatedTeacher = teacherRepository.save(existingTeacher);
            return teacherDTOMapper.apply(updatedTeacher);
    }

    public void deleteTeacherById(int teacherId) {
        checkIfTeacherExistsOrThrow(teacherId);
        teacherRepository.deleteById(teacherId);
        teacherRepository.deleteTeacherSubjectsByTeacherId(teacherId); // Delete related records from teacher_subjects table
    }

    private void checkIfTeacherExistsOrThrow(int teacherId) {
        if (!teacherRepository.existsById(teacherId)){
            throw new ResourceNotFoundException("Teacher not found");
        }
    }

    public List<Subject> getSubjectsByTeacherId(int teacherId) {
        return teacherRepository.findSubjectsByTeacherId(teacherId);
    }

    private void setTeacherSubjects(TeacherDTO teacher) {
        List<Subject> subjects = getSubjectsByTeacherId(teacher.getId());
        List<String> subjectNames = subjects.stream()
                .map(Subject::getSubject)
                .collect(Collectors.toList());
        teacher.setSubjectNames(subjectNames);
    }
}
