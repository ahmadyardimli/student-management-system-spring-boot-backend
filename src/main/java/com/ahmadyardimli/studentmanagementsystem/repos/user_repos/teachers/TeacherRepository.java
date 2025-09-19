package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.teachers;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.teachers.Teacher;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsByUser_Id(int userId);
    Optional<Teacher> findByUserId(Integer userId);
    @Query("SELECT t.subjects FROM Teacher t WHERE t.id = :teacherId")
    List<Subject> findSubjectsByTeacherId(@Param("teacherId") int teacherId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM teacher_subject WHERE teacher_id = :teacherId", nativeQuery = true)
    void deleteTeacherSubjectsByTeacherId(int teacherId);
}
