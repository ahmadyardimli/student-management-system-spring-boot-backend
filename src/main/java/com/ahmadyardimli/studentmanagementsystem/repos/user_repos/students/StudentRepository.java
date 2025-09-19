package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.students;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.students.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByUser_Id(int userId);
    Optional<Student> findByUserId(Integer userId);
    boolean existsByStudentCode(String studentCode);
    boolean existsByStudentCodeAndIdNot(String studentCode, Integer id);

    @Query("""
        SELECT s
        FROM Student s
        LEFT JOIN s.user u
        LEFT JOIN u.userStatus us
        LEFT JOIN u.userType ut
        LEFT JOIN s.studentType st
        LEFT JOIN s.category c
        LEFT JOIN s.foreignLanguage fl
        LEFT JOIN s.section sb
        LEFT JOIN s.group g
        LEFT JOIN s.subGroup ag
        LEFT JOIN s.classNumber cn
        LEFT JOIN s.classLetter cl
        LEFT JOIN s.studentCommunicationSenderStatus scs
        WHERE
          (:studentTypeId IS NULL OR st.id = :studentTypeId)
          AND (:categoryId IS NULL OR c.id = :categoryId)
          AND (:foreignLanguageId IS NULL OR fl.id = :foreignLanguageId)
          AND (:bolmeId IS NULL OR sb.id = :bolmeId)
          AND (:groupId IS NULL OR g.id = :groupId)
          AND (:altGroupId IS NULL OR ag.id = :altGroupId)
          AND (:communicationStatusId IS NULL OR scs.id = :communicationStatusId)
          AND (:classNumberId IS NULL OR cn.id = :classNumberId)
          AND (:classLetterId IS NULL OR cl.id = :classLetterId)

          /* NEW: filter by userStatusId or userTypeId if not null */
          AND (:userStatusId IS NULL OR us.id = :userStatusId)
          AND (:userTypeId IS NULL OR ut.id = :userTypeId)
    """)
    List<Student> filterStudents(
            @Param("studentTypeId") Integer studentTypeId,
            @Param("categoryId") Integer categoryId,
            @Param("foreignLanguageId") Integer foreignLanguageId,
            @Param("bolmeId") Integer bolmeId,
            @Param("groupId") Integer groupId,
            @Param("altGroupId") Integer altGroupId,
            @Param("communicationStatusId") Integer communicationStatusId,
            @Param("classNumberId") Integer classNumberId,
            @Param("classLetterId") Integer classLetterId,

            // NEW
            @Param("userStatusId") Integer userStatusId,
            @Param("userTypeId") Integer userTypeId
    );
}
