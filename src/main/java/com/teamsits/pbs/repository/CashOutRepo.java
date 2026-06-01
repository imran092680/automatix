package com.teamsits.pbs.repository;

import com.teamsits.pbs.entities.CashOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashOutRepo extends JpaRepository<CashOut, Long> {
    @Query("SELECT co FROM CashOut co WHERE co.transactionDate = :date AND co.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO ORDER BY co.transactionDate ASC, co.id ASC")
    List<CashOut> findCashOutsByDateWhereIsDeletedEqualsZero(@Param("date") LocalDate date);

    @Query("SELECT co FROM CashOut co WHERE co.id = :ebId AND co.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<CashOut> findCashOutByIdWhereIsDeletedEqualsZero(@Param("ebId") Long id);
}
