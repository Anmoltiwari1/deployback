package com.example.UC18.Authentication.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
 
import java.security.Key;
import java.util.Date;
 
/**
 * JwtUtil
 * ──────────────────────────────────────────────────────────────────────────────
 * Responsible for:
 *   1. Generating a JWT after successful login / OAuth2
 *   2. Extracting the username (subject) from a token
 *   3. Validating the token (signature + expiry)
 *
 * The secret key and expiry are read from application.properties so you never
 * hard-code credentials in source.
 */
@Component
public class JwtUtil {
 
    private final Key signingKey;
    private final long expirationMs;
 
    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        // Keys.hmacShaKeyFor() creates a strong HMAC-SHA key from the bytes
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }
 
    // ── Generate ──────────────────────────────────────────────────────────────
 
    /**
     * Creates a signed JWT.
     * Subject = username (or email for OAuth2 users).
     * We also embed the user's role as a custom claim so downstream code can
     * do role-based checks without hitting the DB again.
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }
 
    // ── Extract ───────────────────────────────────────────────────────────────
 
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }
 
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }
 
    // ── Validate ──────────────────────────────────────────────────────────────
 
    /**
     * Returns true only if the token has a valid signature AND hasn't expired.
     * Any tampering with the payload will cause a SignatureException.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);  // throws on invalid / expired
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid or expired — let the filter reject the request
            return false;
        }
    }
 
    // ── Private helper ────────────────────────────────────────────────────────
 
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}