package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceivableRepo extends JpaRepository<Receivable, Long> {
    @Query("SELECT r FROM Receivable r WHERE r.transactionDate = :date AND r.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO ORDER BY r.transactionDate ASC, r.id ASC")
    List<Receivable> findReceivablesByDateWhereIsDeletedEqualsZero(@Param("date") LocalDate date);

    @Query("SELECT r FROM Receivable r WHERE r.id = :rId AND r.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<Receivable> findReceivableByIdWhereIsDeletedEqualsZero(@Param("rId") Long id);
}
