package com.ahmadyardimli.studentmanagementsystem.services.auth_services;

import com.ahmadyardimli.studentmanagementsystem.responses.AuthResponse;
import com.ahmadyardimli.studentmanagementsystem.common.CommonUser;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User; // <-- added
import com.ahmadyardimli.studentmanagementsystem.requests.auth_requests.AuthenticationRequest;
import com.ahmadyardimli.studentmanagementsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// this creates and returns access/refresh tokens for both Admins and Users
// login checks the username and password, then gives back a new access token and a new plain refresh token
// issue tokens is used by refresh endpoint.
// After the existing refresh token is checked and rotated, it returns: a new access token and a new refresh token (the next plain string to store)

// login issues the first pair of tokens. Later, refresh swaps the
// old refresh token for a new one and also gives you a fresh access token.
public class AuthService<T extends CommonUser> {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse register(T entity, String username, String password) {
        return authenticateAndGenerateToken(username, password, entity);
    }

    // Login with username and password. Returns access token + new refresh token (raw)
    public ResponseEntity<?> login(AuthenticationRequest authenticationRequest, T entity) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    );

            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Access token includes roles (generated from Authentication)
            String accessJwt = jwtTokenProvider.generateJwtToken(auth);

            // Persist/rotate refresh (stores only hash) and return raw to client
            String refreshRaw = refreshTokenService.createRefreshToken(entity);

            // Extract role string for client (first authority is fine here)
            String role = auth.getAuthorities().iterator().next().getAuthority();

            // Determine userType for USERS (null for admins)
            String userType = null;
            if (entity instanceof User u) {
                userType = (u.getUserType() != null) ? u.getUserType().getType() : null;
            }

            AuthResponse res = new AuthResponse();
            res.setMessage("Login successful.");
            res.setAccessToken("Bearer " + accessJwt);
            res.setRefreshToken(refreshRaw);
            res.setSubjectId(entity.getId());
            res.setRole(role);
            res.setUserType(userType); // <-- added

            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
        }
    }

    public ResponseEntity<?> issueTokens(CommonUser principal, String newRefreshRaw) {
        // role string by principal type (for client + optional claim)
        String role = (principal instanceof Admin) ? "ROLE_ADMIN" : "ROLE_USER";

        // Build a new access token without re-authenticating (subject + role claim)
        String newAccessJwt = jwtTokenProvider.generateJwtTokenByUserIdAndRole(principal.getId(), role);

        // Determine userType for USERS (null for admins)
        String userType = null;
        if (principal instanceof User u) {
            userType = (u.getUserType() != null) ? u.getUserType().getType() : null;
        }

        AuthResponse res = new AuthResponse();
        res.setMessage("Token successfully refreshed.");
        res.setAccessToken("Bearer " + newAccessJwt);
        res.setRefreshToken(newRefreshRaw);
        res.setSubjectId(principal.getId());
        res.setRole(role);
        res.setUserType(userType); // <-- added

        return ResponseEntity.ok(res);
    }

    private AuthResponse authenticateAndGenerateToken(String username, String password, T savedEntity) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessJwt = jwtTokenProvider.generateJwtToken(auth);
        String refreshRaw = refreshTokenService.createRefreshToken(savedEntity);

        AuthResponse res = new AuthResponse();
        res.setMessage(savedEntity.getClass().getSimpleName() + " successfully registered.");
        res.setAccessToken("Bearer " + accessJwt);
        res.setRefreshToken(refreshRaw);
        res.setSubjectId(savedEntity.getId());

        String role = auth.getAuthorities().isEmpty()
                ? ((savedEntity instanceof Admin) ? "ROLE_ADMIN" : "ROLE_USER")
                : auth.getAuthorities().iterator().next().getAuthority();
        res.setRole(role);

        // Determine userType for USERS (null for admins)
        String userType = null;
        if (savedEntity instanceof User u) {
            userType = (u.getUserType() != null) ? u.getUserType().getType() : null;
        }
        res.setUserType(userType);

        return res;
    }
}