package com.example.UC18.Authentication.security;

import com.example.UC18.Authentication.entity.User;
import com.example.UC18.Authentication.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
/**
 * UserDetailsServiceImpl
 * ──────────────────────────────────────────────────────────────────────────────
 * Spring Security calls loadUserByUsername() during form login / password auth.
 * We load from our `users` table and wrap it in Spring's UserDetails contract.
 *
 * Note: OAuth2 users bypass this — they're handled by OAuth2SuccessHandler.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    private final UserRepository userRepository;
 
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
 
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword() != null ? user.getPassword() : "",  // OAuth2 users have no password
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
