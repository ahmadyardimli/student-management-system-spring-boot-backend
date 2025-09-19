package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Subject findBySubject(String subject);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM teacher_subject WHERE subject_id = :subjectId", nativeQuery = true)
    void deleteTeacherSubjectBySubjectId(@Param("subjectId") int subjectId);
    @Query("SELECT s FROM Subject s WHERE LOWER(s.subject) = LOWER(:newSubject)")
    Subject findBySubjectIgnoreCase(@Param("newSubject") String newSubject);
}
