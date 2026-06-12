package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.AppUser;
import com.teamsits.automatix.entities.Organization;
import com.teamsits.automatix.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT u FROM AppUser u WHERE u.organization = :org AND u.isDeleted = 0 ORDER BY u.fullName ASC")
    List<AppUser> findByOrganization(Organization org);

    boolean existsByUsername(String username);

    boolean existsByRole(UserRole role);
}
