package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesRepo extends JpaRepository<Sales, Long> {

    @Query("SELECT s FROM Sales s WHERE s.organization.id = :orgId AND s.transactionDate = :date AND s.isDeleted = 0 ORDER BY s.transactionDate ASC, s.id ASC")
    List<Sales> findSalesInfosByDateWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("date") LocalDate date);

    @Query("SELECT si FROM Sales si WHERE si.organization.id = :orgId AND si.id = :siId AND si.isDeleted = 0")
    Optional<Sales> findSalesInfoByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("siId") Long id);
}
