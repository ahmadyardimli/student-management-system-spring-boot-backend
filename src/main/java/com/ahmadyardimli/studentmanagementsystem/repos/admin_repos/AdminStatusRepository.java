package com.ahmadyardimli.studentmanagementsystem.repos.admin_repos;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminStatusRepository extends JpaRepository<AdminStatus, Integer> {
    AdminStatus findByStatus(String status);
}
