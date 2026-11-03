package com.smartexam.security.service;

import com.smartexam.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public CustomUserDetails getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new IllegalStateException("Invalid authenticated user");
        }

        return userDetails;
    }

    public Long getUserId() {
        return getCurrentUser().getUser().getId();
    }

    public Long getOrganizationId() {
        return getCurrentUser().getUser().getOrganization().getId();
    }

    public String getRole() {
        return getCurrentUser().getUser().getRole().getName();
    }

    public String getEmail() {
        return getCurrentUser().getUsername();
    }

    public boolean hasRole(String role) {
        return getCurrentUser().getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isOrganizationAdmin() {
        return hasRole("ORGANIZATION_ADMIN");
    }

    public boolean isTeacher() {
        return hasRole("TEACHER");
    }

    public boolean isStudent() {
        return hasRole("STUDENT");
    }
}