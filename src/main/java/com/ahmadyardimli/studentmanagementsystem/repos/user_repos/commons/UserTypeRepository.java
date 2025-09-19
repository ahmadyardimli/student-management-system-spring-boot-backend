package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    UserType findByType(String type);
    @Query("SELECT u FROM UserType u WHERE LOWER(u.type) = LOWER(:newType)")
    UserType findByTypeIgnoreCase(@Param("newType") String newType);
}
