package com.smartexam.institution.campuses;

import com.smartexam.institution.campuses.entity.Campus;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class CampusEntityListener {

    @PrePersist
    public void onCreate(Campus campus) {

        LocalDateTime now = LocalDateTime.now();

        if (campus.getCreatedAt() == null) {
            campus.setCreatedAt(now);
        }

        if (campus.getUpdatedAt() == null) {
            campus.setUpdatedAt(now);
        }

        if (campus.getCreatedBy() == null ||
                campus.getCreatedBy().isBlank()) {

            campus.setCreatedBy(getCurrentUsername());
        }

        if (campus.getUpdatedBy() == null ||
                campus.getUpdatedBy().isBlank()) {

            campus.setUpdatedBy(getCurrentUsername());
        }

        if (campus.getActive() == null) {
            campus.setActive(true);
        }
    }

    @PreUpdate
    public void onUpdate(Campus campus) {

        campus.setUpdatedAt(LocalDateTime.now());
        campus.setUpdatedBy(getCurrentUsername());
    }

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "SYSTEM";
        }

        String username = authentication.getName();

        if (username == null ||
                username.isBlank()) {

            return "SYSTEM";
        }

        return username;
    }
}