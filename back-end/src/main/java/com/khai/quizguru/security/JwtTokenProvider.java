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

/**
 * Utility class for handling JWT (JSON Web Token) generation, parsing, and validation.
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.jwtExpiration}")
    private Long jwtExpirationMs;

    /**
     * Generates a JWT from the provided user ID and roles.
     *
     * @param userId The ID of the user.
     * @param roles  The roles associated with the user.
     * @return The generated JWT.
     */
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

    /**
     * Generates a JWT from the provided authentication information.
     *
     * @param auth The authentication object.
     * @return The generated JWT.
     */
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

    /**
     * Resolves the JWT token from the provided HTTP servlet request.
     *
     * @param request The HTTP servlet request.
     * @return The resolved JWT token.
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Retrieves the user ID from the provided JWT token.
     *
     * @param jwt The JWT token.
     * @return The user ID extracted from the JWT token.
     */
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


