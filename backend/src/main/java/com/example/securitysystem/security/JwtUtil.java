package com.example.securitysystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * Responsible for generating, validating, and parsing tokens.
 */
@Component
public class JwtUtil {

    // Secure secret key generation for signing tokens
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a new JWT token for a given user.
     *
     * @param email The user's email (subject).
     * @param role  The user's role (Manager/Employee).
     * @return A signed JWT string.
     */
    public String generateToken(String email, String role) {
        // Token expiration time: 5 minutes (as per requirements)
        long EXPIRATION_TIME = 5 * 60 * 1000;
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the user email (subject) from a JWT token.
     *
     * @param token The JWT token.
     * @return The email contained in the token subject.
     */
    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates the JWT token.
     * Checks signature and expiration.
     *
     * @param token The JWT token to validate.
     * @return true if valid, false if expired or tampered with.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generic method to extract a specific claim from the token.
     *
     * @param token          The JWT token.
     * @param claimsResolver Function to resolve the claim.
     * @param <T>            The type of the claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}