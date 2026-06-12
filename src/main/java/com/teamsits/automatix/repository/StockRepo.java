package com.teamsits.automatix.repository;

import com.teamsits.automatix.entities.Stock;
import com.teamsits.automatix.models.stock.StockPerProductByDateResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockRepo extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT " +
            "  p.id AS productId, " +
            "  p.name AS productName, " +
            "  COALESCE(SUM(CASE " +
            "                 WHEN s.transaction_date < :transactionDate AND s.stock_transaction_type = 'PURCHASE' THEN s.quantity " +
            "                 WHEN s.transaction_date < :transactionDate AND s.stock_transaction_type = 'SALES' THEN -s.quantity " +
            "                 ELSE 0 " +
            "    END), 0) AS openingStock, " +
            "  COALESCE(SUM(CASE " +
            "                 WHEN s.transaction_date =  :transactionDate AND s.stock_transaction_type = 'PURCHASE' THEN s.quantity " +
            "                 ELSE 0 " +
            "    END), 0) AS purchased, " +
            "  COALESCE(SUM(CASE " +
            "                 WHEN s.transaction_date =  :transactionDate AND s.stock_transaction_type = 'SALES' THEN s.quantity " +
            "                 ELSE 0 " +
            "    END), 0) AS sold, " +
            "  COALESCE(SUM(CASE " +
            "                 WHEN s.transaction_date <=  :transactionDate AND s.stock_transaction_type = 'PURCHASE' THEN s.quantity " +
            "                 WHEN s.transaction_date <=  :transactionDate AND s.stock_transaction_type = 'SALES' THEN -s.quantity " +
            "                 ELSE 0 " +
            "    END), 0) AS remainingStock " +
            "FROM product p " +
            "LEFT JOIN stock s ON s.product_id = p.id " +
            "AND s.is_deleted = 0 " +
            "GROUP BY p.id, p.name " +
            "HAVING COALESCE(SUM(CASE " +
            "                 WHEN s.transaction_date <=  :transactionDate AND s.stock_transaction_type = 'PURCHASE' THEN s.quantity " +
            "                 WHEN s.transaction_date <=  :transactionDate AND s.stock_transaction_type = 'SALES' THEN -s.quantity " +
            "                 ELSE 0 " +
            "    END), 0) > 0 " +
            "ORDER BY p.name",
            nativeQuery = true)
    List<StockPerProductByDateResponse> getStockDataByDate(@Param("transactionDate") LocalDate transactionDate);

    @Query("select s from Stock s where s.product.id = :productId and s.isDeleted = 0 and s.transactionDate <= :date")
    List<Stock> findStocksByProductByDate(@Param("productId") Long productId, @Param("date") LocalDate date);
}
