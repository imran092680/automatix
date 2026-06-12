package com.teamsits.automatix.repository.master_data_repository;

import com.teamsits.automatix.entities.master_entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyRepo extends JpaRepository<Party, Long> {

    @Query("SELECT p FROM Party p WHERE p.organization.id = :orgId AND p.isDeleted = 0 ORDER BY p.name ASC")
    List<Party> findPartiesWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT p FROM Party p WHERE p.organization.id = :orgId AND p.partyType = com.teamsits.automatix.enums.PartyType.PURCHASE_PARTY AND p.isDeleted = 0")
    List<Party> findPurchasePartiesWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT p FROM Party p WHERE p.organization.id = :orgId AND p.partyType = com.teamsits.automatix.enums.PartyType.SALES_PARTY AND p.isDeleted = 0")
    List<Party> findSalesPartiesWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT p FROM Party p WHERE p.organization.id = :orgId AND p.partyType = com.teamsits.automatix.enums.PartyType.PURCHASE_PARTY AND p.isDeleted = 0")
    Page<Party> findPageByIsDeletedEqualsZero(@Param("orgId") Long orgId, Pageable pageable);

    @Query("SELECT p FROM Party p WHERE p.organization.id = :orgId AND p.id = :piId AND p.isDeleted = 0")
    Optional<Party> findPartyByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("piId") Long id);

    boolean existsByOrganizationIdAndName(Long organizationId, String name);
}
