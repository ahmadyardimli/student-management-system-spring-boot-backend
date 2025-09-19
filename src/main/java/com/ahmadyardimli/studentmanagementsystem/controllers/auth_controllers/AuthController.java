package com.ahmadyardimli.studentmanagementsystem.controllers.auth_controllers;

import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.requests.auth_requests.AuthenticationRequest;
import com.ahmadyardimli.studentmanagementsystem.requests.auth_requests.RefreshRequest;
import com.ahmadyardimli.studentmanagementsystem.responses.ApiError;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.admins.AdminService;
import com.ahmadyardimli.studentmanagementsystem.services.admin_services.users.commons.UserService;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.AuthService;
import com.ahmadyardimli.studentmanagementsystem.services.auth_services.RefreshTokenService;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RefreshTokenExpiredException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RefreshTokenInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final int ACTIVE_STATUS_ID = 0; // in database active status is 0
    private final UserService userService;
    private final AdminService adminService;
    private final AuthService<User> userAuthService;
    private final AuthService<Admin> adminAuthService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService,
                          AdminService adminService,
                          AuthService<User> userAuthService,
                          AuthService<Admin> adminAuthService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.adminService = adminService;
        this.userAuthService = userAuthService;
        this.adminAuthService = adminAuthService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();

            User user = userService.getUserByUsername(username);
            if (user != null) {
                if (user.getUserStatus() == null || user.getUserStatus().getId() != ACTIVE_STATUS_ID) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiError("account_inactive", "User is inactive."));
                }
                return userAuthService.login(authenticationRequest, user);
            }

            Admin admin = adminService.getAdminByUsername(username);
            if (admin != null) {
                if (admin.getStatus() == null || admin.getStatus().getId() != ACTIVE_STATUS_ID) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiError("account_inactive", "Admin is inactive."));
                }
                return adminAuthService.login(authenticationRequest, admin);
            }

            // No user or admin with that username
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("incorrect_username", "Incorrect username."));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("auth_failed", ex.getMessage()));
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {
        try {
            var rotationResult = refreshTokenService.validateAndRotateByToken(req.getRefreshToken());

            switch (rotationResult.type) {
                case ADMIN -> {
                    Admin a = adminService.findAdminOrNull(rotationResult.subjectId);
                    if (a == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError("user_not_found", "Admin not found."));
                    }
                    if (a.getStatus().getId() != ACTIVE_STATUS_ID) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError("account_inactive", "Admin is inactive."));
                    }
                    return adminAuthService.issueTokens(a, rotationResult.newRefreshToken);
                }
                case USER -> {
                    User u = userService.findUserOrNull(rotationResult.subjectId);
                    if (u == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError("user_not_found", "User not found."));
                    }
                    if (u.getUserStatus().getId() != ACTIVE_STATUS_ID) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError("account_inactive", "User is inactive."));
                    }
                    return userAuthService.issueTokens(u, rotationResult.newRefreshToken);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("token_invalid", "Invalid refresh token."));
        } catch (RefreshTokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("token_expired", "Refresh token expired."));
        } catch (RefreshTokenInvalidException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("token_invalid", "Invalid refresh token."));
        }
    }
}