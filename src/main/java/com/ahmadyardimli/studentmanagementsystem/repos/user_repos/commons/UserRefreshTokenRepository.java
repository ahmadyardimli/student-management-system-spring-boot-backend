package com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons;

import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {
    UserRefreshToken findByUserId(int userId);
    UserRefreshToken findByTokenHash(String tokenHash);
}
