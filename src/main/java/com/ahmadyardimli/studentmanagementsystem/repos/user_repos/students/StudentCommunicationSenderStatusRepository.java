package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.StudentCommunicationSenderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCommunicationSenderStatusRepository extends JpaRepository<StudentCommunicationSenderStatus, Integer> {
    StudentCommunicationSenderStatus findByStatus(String status);
}
