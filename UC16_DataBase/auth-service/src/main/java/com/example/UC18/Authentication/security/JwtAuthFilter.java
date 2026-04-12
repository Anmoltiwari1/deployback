package com.example.UC18.Authentication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;
import java.util.List;
 
/**
 * JwtAuthFilter
 * ──────────────────────────────────────────────────────────────────────────────
 * Runs once per HTTP request (OncePerRequestFilter guarantee).
 *
 * Flow:
 *   Request → read "Authorization: Bearer <token>" header
 *           → validate token with JwtUtil
 *           → set authentication in SecurityContextHolder
 *           → continue filter chain
 *
 * If no valid token is found, the request is just passed along unauthenticated
 * and Spring Security will reject it at the route level if the route requires auth.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    private final JwtUtil jwtUtil;
 
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
 
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
 
        String authHeader = request.getHeader("Authorization");
 
        // Header must start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
 
        String token = authHeader.substring(7); // strip "Bearer "
 
        if (!jwtUtil.validateToken(token)) {
            // Token is expired or tampered — just continue (unauthenticated)
            filterChain.doFilter(request, response);
            return;
        }
 
        // Token is valid — extract claims and set authentication
        String username = jwtUtil.extractUsername(token);
        String role     = jwtUtil.extractRole(token);
 
        // Only set if not already authenticated (prevents double-processing)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
 
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );
 
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
 
        filterChain.doFilter(request, response);
    }
}
