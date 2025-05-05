package com.gotrid.trid.marshmello.utils;

import com.example.metamall.models.Entities.User;
import com.example.metamall.repository.UserRepository;
import com.example.metamall.security.jwt.JwtUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorizationUtil {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthorizationUtil(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }
    public Long extractUserIdFromToken(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            return jwtUtils.getUserIdFromJwtToken(authHeader.substring(7));
        } catch (Exception e) {
            System.err.println("Auth error: " + e.getMessage());
            return null;
        }
    }

    public Long validateUserRole(String authHeader, String requiredRole) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return null;
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().toUpperCase().trim())
                .collect(Collectors.toSet());

        String normalizedRequiredRole = requiredRole.toUpperCase().trim();
        boolean hasRequiredRole = userRoles.contains(normalizedRequiredRole);
        boolean isAdmin = userRoles.contains("ROLE_ADMIN");

        return (isAdmin || hasRequiredRole) ? userId : null;
    }
}