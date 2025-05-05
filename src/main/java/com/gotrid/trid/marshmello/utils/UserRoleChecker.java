package com.gotrid.trid.marshmello.utils;

import com.example.metamall.models.Entities.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRoleChecker {

    public boolean isClient(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }

        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().toUpperCase().trim())
                .collect(Collectors.toSet());

        return userRoles.contains("ROLE_CLIENT");
    }

    public boolean isAdmin(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }

        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().toUpperCase().trim())
                .collect(Collectors.toSet());

        return userRoles.contains("ROLE_ADMIN");
    }
}
