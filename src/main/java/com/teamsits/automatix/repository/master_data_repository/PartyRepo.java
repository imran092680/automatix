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
    @Query("SELECT p FROM Party p WHERE p.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO  ORDER BY p.name ASC")
    List<Party> findPartiesWhereIsDeletedEqualsZero();

    @Query("SELECT p FROM Party p WHERE p.partyType = com.teamsits.automatix.enums.PartyType.PURCHASE_PARTY AND p.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    List<Party> findPurchasePartiesWhereIsDeletedEqualsZero();

    @Query("SELECT p FROM Party p WHERE p.partyType = com.teamsits.automatix.enums.PartyType.SALES_PARTY AND p.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    List<Party> findSalesPartiesWhereIsDeletedEqualsZero();

    @Query("SELECT p FROM Party p WHERE p.partyType = com.teamsits.automatix.enums.PartyType.PURCHASE_PARTY AND p.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Page<Party> findPageByIsDeletedEqualsZero(Pageable pageable);

    @Query("SELECT p FROM Party p WHERE p.id = :piId AND p.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<Party> findPartyByIdWhereIsDeletedEqualsZero(@Param("piId") Long id);

    List<Party> findAllByName(String name);

    boolean existsPartiesByName(String name);
}
