package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesRepo extends JpaRepository<Sales, Long> {
    @Query("SELECT s FROM Sales s WHERE s.transactionDate = :date AND s.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO ORDER BY s.transactionDate ASC, s.id ASC")
    List<Sales> findSalesInfosByDateWhereIsDeletedEqualsZero(@Param("date") LocalDate date);

    @Query("SELECT si FROM Sales si WHERE si.id = :siId AND si.isDeleted = com.teamsits.automatix.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<Sales> findSalesInfoByIdWhereIsDeletedEqualsZero(@Param("siId") Long id);
}
