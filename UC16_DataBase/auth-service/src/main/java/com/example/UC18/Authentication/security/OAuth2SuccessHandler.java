package com.example.UC18.Authentication.security;

import com.example.UC18.Authentication.entity.User;
import com.example.UC18.Authentication.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
import java.io.IOException;
 
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
 
    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
 
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
 
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
 
        String email = extractEmail(oAuth2User);
        User user = findOrRegisterUser(email, detectProvider(oAuth2User));
        writeTokenResponse(response, user);
    }
 
    // ── Extract email (works for Google + GitHub) ─────────────────────────────
 
    private String extractEmail(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            // GitHub doesn't always expose email — fall back to login username
            email = oAuth2User.getAttribute("login");
        }
        if (email == null) {
            throw new IllegalStateException("Could not extract email from OAuth2 provider");
        }
        return email;
    }
 
    // ── Detect which provider (google / github / etc.) ────────────────────────
 
    private String detectProvider(OAuth2User oAuth2User) {
        // GitHub users have a "login" attribute, Google users have "sub"
        if (oAuth2User.getAttribute("login") != null) return "github";
        if (oAuth2User.getAttribute("sub") != null)   return "google";
        return "unknown";
    }
 
    // ── Auto-register user on first login, reuse existing on subsequent logins ─
 
    private User findOrRegisterUser(String email, String provider) {
        return userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setPassword(null);       // OAuth2 users have no password
                    newUser.setRole("ROLE_USER");
                    newUser.setProvider(provider);
                    return userRepository.save(newUser);
                });
    }
 
    // ── Write JWT to response body ────────────────────────────────────────────
 
    private void writeTokenResponse(HttpServletResponse response, User user) throws IOException {
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        response.sendRedirect("https://deploy-front-zgs5.vercel.app?token=" + token);
    }
}