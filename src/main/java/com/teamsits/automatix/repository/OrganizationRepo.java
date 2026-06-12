package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepo extends JpaRepository<Organization, Long> {

    @Query("SELECT o FROM Organization o WHERE o.isDeleted = 0 ORDER BY o.name ASC")
    List<Organization> findAllActive();

    @Query("SELECT o FROM Organization o WHERE o.id = :id AND o.isDeleted = 0")
    Optional<Organization> findActiveById(Long id);

    boolean existsByName(String name);
}
