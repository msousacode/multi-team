package com.multiteam.core.security;

import com.multiteam.core.models.UserPrincipal;
import com.multiteam.modules.user.User;
import com.multiteam.modules.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        var username = authentication.getPrincipal() + "";
        var password = authentication.getCredentials() + "";

        UserDetails user = loadUserByUsername(username);

        verifyPassword(password, user);

        if (!user.isEnabled()) {
            throw new DisabledException("User account is not active");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public UserDetails loadUserByUsername(String username) {

        Optional<User> loginOptional = userRepository.findByEmail(username);

        if (loginOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return UserPrincipal.create(loginOptional.get());
    }

    private void verifyPassword(String password, UserDetails user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw   new BadCredentialsException("Bad credentials");
        }
    }
}
