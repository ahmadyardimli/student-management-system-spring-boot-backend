package com.ahmadyardimli.studentmanagementsystem.services.auth_services;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.repos.admin_repos.AdminRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRepository;
import com.ahmadyardimli.studentmanagementsystem.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Role-aware loaders so JwtAuthenticationFilter can fetch the right principal type by id.
@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public UserDetailsServiceCustom(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username);
        if (u != null) return JwtUserDetails.create(u);

        Admin a = adminRepository.findByUsername(username);
        if (a != null) return JwtUserDetails.create(a);

        throw new UsernameNotFoundException("No user or admin found with username '" + username + "'.");
    }

    public UserDetails loadUserByIdOnly(int id) {
        User u = userRepository.findById(id).orElse(null);
        return (u != null) ? JwtUserDetails.create(u) : null;
    }

    public UserDetails loadAdminById(int id) {
        Admin a = adminRepository.findById(id).orElse(null);
        return (a != null) ? JwtUserDetails.create(a) : null;
    }

    // try user, then admin. Returns null if neither found.
    public UserDetails loadUserById(int id) {
        UserDetails u = loadUserByIdOnly(id);
        if (u != null) return u;
        return loadAdminById(id);
    }
}
