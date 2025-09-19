package com.ahmadyardimli.studentmanagementsystem.repos.admin_repos;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
    Admin findByAdminRoleId(int adminRoleId);
}
