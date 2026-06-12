package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.AppUser;
import com.teamsits.automatix.entities.Organization;
import com.teamsits.automatix.enums.UserRole;
import com.teamsits.automatix.repository.AppUserRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public List<AppUser> getUsersInCurrentOrg() {
        return appUserRepo.findByOrganization(securityUtils.getCurrentOrganization());
    }

    public List<AppUser> getUsersByOrg(Organization org) {
        return appUserRepo.findByOrganization(org);
    }

    public AppUser createUser(String username, String plainPassword, String fullName, String email, UserRole role, Organization org) {
        if (appUserRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setOrganization(org);
        user.setIsActive(true);
        user.setCreatedBy(securityUtils.getCurrentUserId());
        user.setUpdatedBy(securityUtils.getCurrentUserId());
        return appUserRepo.save(user);
    }

    public void toggleActive(Long userId) {
        AppUser user = appUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        user.setUpdatedBy(securityUtils.getCurrentUserId());
        appUserRepo.save(user);
    }

    public void deleteUser(Long userId) {
        AppUser user = appUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
        user.setUpdatedBy(securityUtils.getCurrentUserId());
        appUserRepo.save(user);
    }
}
