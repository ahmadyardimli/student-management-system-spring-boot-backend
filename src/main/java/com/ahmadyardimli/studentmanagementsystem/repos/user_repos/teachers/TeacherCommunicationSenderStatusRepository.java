package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.TeacherCommunicationSenderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherCommunicationSenderStatusRepository extends JpaRepository<TeacherCommunicationSenderStatus, Integer> {
    TeacherCommunicationSenderStatus findByStatus(String status);
}
