package com.teamsits.automatix;

import com.teamsits.automatix.entities.AppUser;
import com.teamsits.automatix.entities.Organization;
import com.teamsits.automatix.enums.UserRole;
import com.teamsits.automatix.repository.AppUserRepo;
import com.teamsits.automatix.repository.OrganizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private OrganizationRepo organizationRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${AUTOMATIX_ADMIN_PASSWORD:Admin@123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!appUserRepo.existsByRole(UserRole.SUPER_ADMIN)) {
            Organization defaultOrg = getOrCreateDefaultOrganization();

            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFullName("Super Admin");
            admin.setRole(UserRole.SUPER_ADMIN);
            admin.setOrganization(defaultOrg);
            admin.setIsActive(true);
            admin.setCreatedBy(0L);
            admin.setUpdatedBy(0L);
            appUserRepo.save(admin);
            System.out.println("=================================================");
            System.out.println("SUPER_ADMIN created — username: admin");
            System.out.println("Password: " + adminPassword);
            System.out.println("=================================================");
        } else {
            appUserRepo.findAll().stream()
                    .filter(u -> UserRole.SUPER_ADMIN.equals(u.getRole()) && u.getOrganization() == null)
                    .forEach(u -> {
                        u.setOrganization(getOrCreateDefaultOrganization());
                        appUserRepo.save(u);
                        System.out.println("Assigned default organization to existing SUPER_ADMIN: " + u.getUsername());
                    });
        }
    }

    private Organization getOrCreateDefaultOrganization() {
        return organizationRepo.findAll().stream()
                .filter(o -> "Default".equals(o.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Organization org = new Organization();
                    org.setName("Default");
                    org.setIsActive(true);
                    org.setCreatedBy(0L);
                    org.setUpdatedBy(0L);
                    return organizationRepo.save(org);
                });
    }
}
