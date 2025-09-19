package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGroupRepository extends JpaRepository<SubGroup, Integer> {
    SubGroup findBySubGroup(String subGroup);
    @Query("SELECT a FROM SubGroup a WHERE LOWER(a.subGroup) = LOWER(:newSubGroup)")
    SubGroup findBySubGroupIgnoreCase(@Param("newSubGroup") String newSubGroup);
}
