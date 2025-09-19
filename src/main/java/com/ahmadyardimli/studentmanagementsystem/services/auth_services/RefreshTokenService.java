package com.ahmadyardimli.studentmanagementsystem.services.auth_services;

import com.ahmadyardimli.studentmanagementsystem.common.CommonUser;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.Admin;
import com.ahmadyardimli.studentmanagementsystem.entities.admin_entities.AdminRefreshToken;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.User;
import com.ahmadyardimli.studentmanagementsystem.entities.user_entities.commons.UserRefreshToken;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RefreshTokenExpiredException;
import com.ahmadyardimli.studentmanagementsystem.exceptions.RefreshTokenInvalidException;
import com.ahmadyardimli.studentmanagementsystem.repos.admin_repos.AdminRefreshTokenRepository;
import com.ahmadyardimli.studentmanagementsystem.repos.user_repos.commons.UserRefreshTokenRepository;
import com.ahmadyardimli.studentmanagementsystem.utils.TokenHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class RefreshTokenService {
    @Value("${sms.refresh.token.expires.in}")
    private long expireSeconds;

    private final UserRefreshTokenRepository userRepo;
    private final AdminRefreshTokenRepository adminRepo;

    public RefreshTokenService(UserRefreshTokenRepository userRepo,
                               AdminRefreshTokenRepository adminRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }


    @Transactional
    public String createRefreshToken(CommonUser commonUser) {
        if (commonUser instanceof User u)   return createOrReplaceUser(u);
        if (commonUser instanceof Admin a)  return createOrReplaceAdmin(a);
        throw new IllegalArgumentException("Unknown user type: " + commonUser.getClass());
    }

    private String createOrReplaceUser(User user) {
        var tokenRecord = userRepo.findByUserId(user.getId());
        if (tokenRecord == null) {
            tokenRecord = new UserRefreshToken();
            tokenRecord.setUser(user);
        }
        String raw = newToken();
        tokenRecord.setTokenHash(TokenHash.sha256Hex(raw));
        tokenRecord.setExpiryDate(expiresAt());
        userRepo.save(tokenRecord);
        return raw; // return Rraw to client and only hash is stored
    }

    private String createOrReplaceAdmin(Admin admin) {
        var tokenRecord = adminRepo.findByAdminId(admin.getId());
        if (tokenRecord == null) {
            tokenRecord = new AdminRefreshToken();
            tokenRecord.setAdmin(admin);
        }
        String raw = newToken();
        tokenRecord.setTokenHash(TokenHash.sha256Hex(raw));
        tokenRecord.setExpiryDate(expiresAt());
        adminRepo.save(tokenRecord);
        return raw; // return raw to client and only hash is stored
    }

    public enum SubjectType { USER, ADMIN }

    public static final class RotationResult {
        public final SubjectType type;
        public final int subjectId;          // User.id or Admin.id
        public final String newRefreshToken; // raw token to return to client

        private RotationResult(SubjectType type, int subjectId, String newRefreshToken) {
            this.type = type;
            this.subjectId = subjectId;
            this.newRefreshToken = newRefreshToken;
        }

        public static RotationResult user(int id, String tok)  { return new RotationResult(SubjectType.USER, id, tok); }
        public static RotationResult admin(int id, String tok) { return new RotationResult(SubjectType.ADMIN, id, tok); }
    }

    @Transactional
    public RotationResult validateAndRotateByToken(String incomingRawToken) {
        String tokenHash = TokenHash.sha256Hex(incomingRawToken);

        var adminTokenRecord = adminRepo.findByTokenHash(tokenHash);
        if (adminTokenRecord != null) {
            if (isExpired(adminTokenRecord.getExpiryDate())) throw new RefreshTokenExpiredException();
            String newRaw = newToken();
            adminTokenRecord.setTokenHash(TokenHash.sha256Hex(newRaw));
            adminTokenRecord.setExpiryDate(expiresAt());
            adminRepo.save(adminTokenRecord);

            int adminId = adminTokenRecord.getAdmin().getId();
            return RotationResult.admin(adminId, newRaw);
        }

        var userTokenRec = userRepo.findByTokenHash(tokenHash);
        if (userTokenRec != null) {
            if (isExpired(userTokenRec.getExpiryDate())) throw new RefreshTokenExpiredException();
            String newRaw = newToken();
            userTokenRec.setTokenHash(TokenHash.sha256Hex(newRaw));
            userTokenRec.setExpiryDate(expiresAt());
            userRepo.save(userTokenRec);

            int userId = userTokenRec.getUser().getId();
            return RotationResult.user(userId, newRaw);
        }

        throw new RefreshTokenInvalidException();
    }

    public boolean isExpired(LocalDateTime expiry) {
        return expiry.isBefore(LocalDateTime.now());
    }

    @Transactional
    public void revokeAllForUser(int userId) {
        var rec = userRepo.findByUserId(userId);
        if (rec != null) userRepo.delete(rec);
    }

    @Transactional
    public void revokeAllForAdmin(int adminId) {
        var rec = adminRepo.findByAdminId(adminId);
        if (rec != null) adminRepo.delete(rec);
    }

    private String newToken() { return java.util.UUID.randomUUID().toString(); }

    private LocalDateTime expiresAt() {
        return LocalDateTime.now().plusSeconds(expireSeconds);
    }
}
