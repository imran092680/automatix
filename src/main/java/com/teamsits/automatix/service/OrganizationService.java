package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.Organization;
import com.teamsits.automatix.enums.SubscriptionType;
import com.teamsits.automatix.repository.OrganizationRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepo organizationRepo;
    private final SecurityUtils securityUtils;

    public List<Organization> getAllOrganizations() {
        return organizationRepo.findAllActive();
    }

    public Organization getById(Long id) {
        return organizationRepo.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found: " + id));
    }

    public Organization createOrganization(String name, SubscriptionType subscriptionType, Integer maxUsers, String agreementNotes) {
        if (organizationRepo.existsByName(name)) {
            throw new IllegalArgumentException("Organization '" + name + "' already exists.");
        }
        Organization org = new Organization();
        org.setName(name);
        org.setSubscriptionType(subscriptionType != null ? subscriptionType : SubscriptionType.UNLIMITED);
        org.setMaxUsers(maxUsers);
        org.setAgreementNotes(agreementNotes);
        org.setIsActive(true);
        org.setCreatedBy(securityUtils.getCurrentUserId());
        org.setUpdatedBy(securityUtils.getCurrentUserId());
        return organizationRepo.save(org);
    }

    public void deactivateOrganization(Long id) {
        Organization org = getById(id);
        org.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
        organizationRepo.save(org);
    }
}
