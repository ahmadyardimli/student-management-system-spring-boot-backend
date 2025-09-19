package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
        Section findBySection(String section);
        @Query("SELECT b FROM Section b WHERE LOWER(b.section) = LOWER(:newSection)")
        Section findBySectionIgnoreCase(@Param("newSection") String newSection);
}
