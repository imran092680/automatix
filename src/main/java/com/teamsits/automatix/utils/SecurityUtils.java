package com.teamsits.automatix.utils;

import com.teamsits.automatix.entities.AppUser;
import com.teamsits.automatix.entities.Organization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in context");
        }
        return (AppUser) auth.getPrincipal();
    }

    public Organization getCurrentOrganization() {
        return getCurrentUser().getOrganization();
    }

    public Long getCurrentOrganizationId() {
        return getCurrentOrganization().getId();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
