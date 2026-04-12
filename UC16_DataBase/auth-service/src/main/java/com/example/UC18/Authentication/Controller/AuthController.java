package com.example.UC18.Authentication.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.UC18.Authentication.entity.User;
import com.example.UC18.Authentication.repository.UserRepository;
import com.example.UC18.Authentication.security.JwtUtil;

/**
 * AuthController
 * ──────────────────────────────────────────────────────────────────────────────
 * Handles classic username/password auth.
 *
 * POST /auth/register   → creates a new user, returns JWT
 * POST /auth/login      → authenticates, returns JWT
 *
 * Both endpoints are public (configured in SecurityConfig).
 *
 * Request body for both:
 * {
 *   "username": "anmol@example.com",
 *   "password": "mypassword"
 * }
 *
 * Response:
 * {
 *   "token": "eyJhbGci..."
 * }
 */
@RestController
@RequestMapping("/auth")

public class AuthController {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
 
    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
 
    // ── Register ──────────────────────────────────────────────────────────────
 
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
 
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already taken"));
        }
 
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));   // BCrypt hash
        newUser.setRole("ROLE_USER");
        newUser.setProvider("local");
        userRepository.save(newUser);
 
        // Issue JWT immediately so the user doesn't have to log in separately
        String token = jwtUtil.generateToken(username, "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }
 
    // ── Login ─────────────────────────────────────────────────────────────────
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
 
        // AuthenticationManager verifies credentials against UserDetailsService
        // Throws BadCredentialsException if wrong — Spring Security handles the 401
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
 
        // Credentials are correct — load role from DB and issue JWT
        User user = userRepository.findByUsername(username).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
 
        return ResponseEntity.ok(Map.of("token", token));
    }
}