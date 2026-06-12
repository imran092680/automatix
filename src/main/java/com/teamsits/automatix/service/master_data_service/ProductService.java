package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.master_models.ProductModel;
import com.teamsits.automatix.repository.master_data_repository.ProductRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final SecurityUtils securityUtils;

    public List<ProductModel> getProducts() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return productRepo.findProductsWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(ProductModel::new)
                .collect(Collectors.toList());
    }

    public Optional<ProductModel> getProductById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, id).map(ProductModel::new);
    }

    public Optional<ProductModel> addProduct(ProductModel productModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        if (productRepo.existsByOrganizationIdAndName(orgId, productModel.getName())) {
            throw new IllegalArgumentException(productModel.getName() + " already exists.");
        }

        Product product = new Product(productModel);
        product.setOrganization(securityUtils.getCurrentOrganization());
        product.setCreatedBy(securityUtils.getCurrentUserId());
        product.setUpdatedBy(securityUtils.getCurrentUserId());
        return Optional.of(new ProductModel(productRepo.save(product)));
    }

    public void deleteProduct(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (productRepo.existsById(id)) {
            Product product = productRepo
                    .findProductByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("Product not found."));

            product.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            productRepo.save(product);
        }
    }

    public Optional<ProductModel> updateProduct(ProductModel productModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, productModel.getId())
                .orElseThrow(() -> new RuntimeException("This Product does not exist"));

        if (!product.getName().equals(productModel.getName()) &&
                productRepo.existsByOrganizationIdAndName(orgId, productModel.getName())) {
            throw new IllegalArgumentException(productModel.getName() + " already exists.");
        }

        product.setName(productModel.getName());
        product.setUpdatedBy(securityUtils.getCurrentUserId());

        return Optional.of(new ProductModel(productRepo.save(product)));
    }
}
