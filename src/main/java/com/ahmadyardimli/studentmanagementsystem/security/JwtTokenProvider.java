package com.ahmadyardimli.studentmanagementsystem.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
    @Value("${sms.app.secret}")
    private String APP_SECRET;

    @Value("${sms.expires.in}")
    private long EXPIRES_IN;

    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(APP_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication auth) {
        var userDetails = (JwtUserDetails) auth.getPrincipal();
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRES_IN);

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(Integer.toString(userDetails.getId()))
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtTokenByUserId(int userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtTokenByUserIdAndRole(int userId, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .claim("roles", java.util.Set.of(role))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public int getUserIdFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    // read roles claim so the filter can choose Admin vs User.
    public Set<String> getRolesFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object raw = claims.get("roles");
        if (raw == null) return java.util.Set.of();

        if (raw instanceof Collection<?>) {
            return ((Collection<?>) raw).stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }
        return java.util.Set.of(String.valueOf(raw));
    }

    boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
