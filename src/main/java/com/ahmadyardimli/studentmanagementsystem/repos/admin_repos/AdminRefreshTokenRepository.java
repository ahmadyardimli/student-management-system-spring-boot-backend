package com.ahmadyardimli.studentmanagementsystem.repos.admin_repos;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRefreshTokenRepository extends JpaRepository<AdminRefreshToken, Integer> {
    AdminRefreshToken findByAdminId(int userId);
    AdminRefreshToken findByTokenHash(String tokenHash);
}
