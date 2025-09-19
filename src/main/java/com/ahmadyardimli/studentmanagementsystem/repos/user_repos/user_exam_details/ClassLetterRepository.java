package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassLetterRepository extends JpaRepository<ClassLetter, Integer> {
    ClassLetter findByLetterValue(String letterValue);
    boolean existsByLetterValue(String letterValue);
}
