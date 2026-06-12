package com.teamsits.automatix.repository.master_data_repository;

import com.teamsits.automatix.entities.master_entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId AND p.isDeleted = 0 ORDER BY p.name ASC")
    List<Product> findProductsWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId AND p.id = :productId AND p.isDeleted = 0")
    Optional<Product> findProductByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("productId") Long id);

    boolean existsByOrganizationIdAndName(Long organizationId, String name);
}
