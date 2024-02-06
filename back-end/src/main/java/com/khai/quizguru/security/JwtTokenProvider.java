package com.khai.quizguru.security;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import com.khai.quizguru.model.user.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.jwtExpiration}")
    private Long jwtExpirationMs;


    public String generateTokenFromUserId(String userId, List<Role> roles) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        Date now = new Date();
        String jws = Jwts.builder()
                .setSubject(userId)
                .claim("role", roles)
                .setExpiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return jws;
    }
    public String generateToken(Authentication auth) {

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Date now = new Date();

        String jws = Jwts.builder()
                .setSubject(userPrincipal.getId())
                .claim("role", userPrincipal.getAuthorities())
                .setExpiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();

        return jws;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public String getUserIdFromJwt(String jwt) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return String.valueOf(claims.getSubject());
    }


    public boolean validateToken(String authToken) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }catch(Exception e){
            return false;
        }

    }
}

