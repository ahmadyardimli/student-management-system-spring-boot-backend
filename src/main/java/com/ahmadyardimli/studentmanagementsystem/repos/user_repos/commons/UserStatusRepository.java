package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    UserStatus findByStatus(String status);
}
