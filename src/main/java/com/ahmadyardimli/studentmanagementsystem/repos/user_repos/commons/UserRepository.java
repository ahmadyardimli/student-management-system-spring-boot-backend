package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByUserType_Id(int userTypeId);
}

