package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.user_exam_details;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.user_exam_details.ClassNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassNumberRepository extends JpaRepository<ClassNumber, Integer> {
    ClassNumber findByNumberValue(int numberValue);
    boolean existsByNumberValue(int numberValue);
}
