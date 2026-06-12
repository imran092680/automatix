package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.CashIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashInRepo extends JpaRepository<CashIn, Long> {

    @Query("SELECT ci FROM CashIn ci WHERE ci.organization.id = :orgId AND ci.transactionDate = :date AND ci.isDeleted = 0 ORDER BY ci.transactionDate ASC, ci.id ASC")
    List<CashIn> findCashInsByDateWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("date") LocalDate date);

    @Query("SELECT ci FROM CashIn ci WHERE ci.organization.id = :orgId AND ci.id = :cId AND ci.isDeleted = 0")
    Optional<CashIn> findCashInByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("cId") Long id);

    @Query(value = "SELECT (" +
            "   COALESCE((SELECT SUM(ci.amount) FROM cash_in ci WHERE ci.transaction_date < :date AND ci.is_deleted = 0 AND ci.organization_id = :orgId), 0) " +
            "+  COALESCE((SELECT SUM(s.amount) FROM sales s WHERE s.transaction_date < :date AND s.is_deleted = 0 AND s.organization_id = :orgId), 0) " +
            "-  COALESCE((SELECT SUM(co.amount) FROM cash_out co WHERE co.transaction_date < :date AND co.is_deleted = 0 AND co.organization_id = :orgId), 0) " +
            "-  COALESCE((SELECT SUM(r.amount) FROM receivable r WHERE r.transaction_date < :date AND r.is_deleted = 0 AND r.organization_id = :orgId), 0)) " +
            "AS opening_balance", nativeQuery = true)
    Double getOpeningBalance(@Param("orgId") Long orgId, @Param("date") LocalDate date);
}
