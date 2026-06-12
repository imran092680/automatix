package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.CashIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashInRepo extends JpaRepository<CashIn, Long> {
    @Query("SELECT ci FROM CashIn ci WHERE ci.transactionDate = :date AND ci.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO ORDER BY ci.transactionDate ASC, ci.id ASC")
    List<CashIn> findCashInsByDateWhereIsDeletedEqualsZero(@Param("date") LocalDate date);

    @Query("SELECT ci FROM CashIn ci WHERE ci.id = :cId AND ci.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<CashIn> findCashInByIdWhereIsDeletedEqualsZero(@Param("cId") Long id);

    @Query(value = "SELECT (" +
            "   COALESCE((SELECT SUM(ci.amount) FROM cash_in ci WHERE ci.transaction_date < :date AND ci.is_deleted = 0), 0) " +
            "+  COALESCE((SELECT SUM(s.amount) FROM sales s WHERE s.transaction_date < :date AND s.is_deleted = 0), 0) " +
            "-  COALESCE((SELECT SUM(co.amount) FROM cash_out co WHERE co.transaction_date < :date AND co.is_deleted = 0), 0) " +
            "-  COALESCE((SELECT SUM(r.amount) FROM receivable r WHERE r.transaction_date < :date AND r.is_deleted = 0), 0)) " +
            "AS opening_balance", nativeQuery = true)
    Double getOpeningBalance(@Param("date") LocalDate date);
}
