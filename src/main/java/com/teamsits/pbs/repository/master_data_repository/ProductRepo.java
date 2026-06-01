package com.teamsits.pbs.repository.master_data_repository;

import com.teamsits.pbs.entities.master_entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO  ORDER BY p.name ASC")
    List<Product> findProductsWhereIsDeletedEqualsZero();

    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<Product> findProductByIdWhereIsDeletedEqualsZero(@Param("productId") Long id);
}
