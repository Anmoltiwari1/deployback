package com.example.UC18.Authentication.security;

import com.example.UC18.Authentication.entity.User;
import com.example.UC18.Authentication.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
import java.io.IOException;
 
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final String redirectUrl;
 
    public OAuth2SuccessHandler(
            JwtUtil jwtUtil, 
            UserRepository userRepository,
            @Value("${app.oauth2.redirect-url}") String redirectUrl
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.redirectUrl = redirectUrl;
    }
 
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        logger.info("[OAuth2] Authentication successful for provider: {}", authentication.getClass().getSimpleName());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        try {
            String email = extractEmail(oAuth2User);
            logger.info("[OAuth2] Extracted identifier: {}", email);
            
            User user = findOrRegisterUser(email, detectProvider(oAuth2User));
            writeTokenResponse(response, user);
        } catch (Exception e) {
            logger.error("[OAuth2] Error during social login processing", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Social login processing failed: " + e.getMessage());
        }
    }
 
    // ── Extract email (works for Google + GitHub) ─────────────────────────────
 
    private String extractEmail(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) email = oAuth2User.getAttribute("login");
        if (email == null) email = oAuth2User.getAttribute("sub"); // Google
        if (email == null && oAuth2User.getAttribute("id") != null) {
            email = String.valueOf(oAuth2User.getAttribute("id"));
        }

        if (email == null) {
            logger.error("[OAuth2] Attributes received: {}", oAuth2User.getAttributes());
            throw new IllegalStateException("Could not extract identity (email/login/id) from provider");
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
		// Redirect to React with token as query param (e.g. http://localhost:3000?token=...)
		response.sendRedirect(redirectUrl + "?token=" + token);
	}
}