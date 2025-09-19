package com.ahmadyardimli.studentmanagementsystem.repos.admin_repos;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer> {
    AdminRole findByRole(String role);
}
