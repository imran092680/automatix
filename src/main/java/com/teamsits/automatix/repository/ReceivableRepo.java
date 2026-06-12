package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Receivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceivableRepo extends JpaRepository<Receivable, Long> {

    @Query("SELECT r FROM Receivable r WHERE r.organization.id = :orgId AND r.transactionDate = :date AND r.isDeleted = 0 ORDER BY r.transactionDate ASC, r.id ASC")
    List<Receivable> findReceivablesByDateWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("date") LocalDate date);

    @Query("SELECT r FROM Receivable r WHERE r.organization.id = :orgId AND r.id = :rId AND r.isDeleted = 0")
    Optional<Receivable> findReceivableByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("rId") Long id);
}
