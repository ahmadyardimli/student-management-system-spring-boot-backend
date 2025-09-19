package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.StudentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTypeRepository extends JpaRepository<StudentType, Integer> {
    StudentType findByType(String type);
    @Query("SELECT s FROM StudentType s WHERE LOWER(s.type) = LOWER(:newType)")
    StudentType findByTypeIgnoreCase(@Param("newType") String newType);
}
