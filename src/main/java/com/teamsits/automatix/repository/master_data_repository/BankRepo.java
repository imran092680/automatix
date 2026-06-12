package com.teamsits.automatix.repository.master_data_repository;

import com.teamsits.automatix.entities.master_entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepo extends JpaRepository<Bank, Long> {
    @Query("SELECT u FROM Bank u WHERE u.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO ORDER BY u.name ASC")
    List<Bank> findBanksWhereIsDeletedEqualsZero();

    @Query("SELECT u FROM Bank u WHERE u.id = :bankId AND u.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<Bank> findBankByIdWhereIsDeletedEqualsZero(@Param("bankId") Long id);
}
