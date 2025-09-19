package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ForeignLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ForeignLanguageRepository extends JpaRepository<ForeignLanguage, Integer> {
    ForeignLanguage findByForeignLanguage(String foreignLanguage);
    @Query("SELECT f FROM ForeignLanguage f WHERE LOWER(f.foreignLanguage) = LOWER(:newLanguage)")
    ForeignLanguage findByForeignLanguageIgnoreCase(@Param("newLanguage") String newLanguage);
}
