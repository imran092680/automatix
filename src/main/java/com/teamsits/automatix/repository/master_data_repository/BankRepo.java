package com.teamsits.automatix.repository.master_data_repository;

import com.teamsits.automatix.entities.master_entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepo extends JpaRepository<Bank, Long> {

    @Query("SELECT u FROM Bank u WHERE u.organization.id = :orgId AND u.isDeleted = 0 ORDER BY u.name ASC")
    List<Bank> findBanksWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT u FROM Bank u WHERE u.organization.id = :orgId AND u.id = :bankId AND u.isDeleted = 0")
    Optional<Bank> findBankByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("bankId") Long id);

    boolean existsByOrganizationIdAndName(Long organizationId, String name);
}
