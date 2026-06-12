package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.master_models.ProductModel;
import com.teamsits.automatix.repository.master_data_repository.ProductRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public List<ProductModel> getProducts() {
        return productRepo.findProductsWhereIsDeletedEqualsZero()
                .stream()
                .map(ProductModel::new)
                .collect(Collectors.toList());
    }

    public Optional<ProductModel> getProductById(Long id) {
        return productRepo.findProductByIdWhereIsDeletedEqualsZero(id).map(ProductModel::new);
    }

    public Optional<ProductModel> addProduct(ProductModel productModel) {
        boolean exists = productRepo.findProductsWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productModel.getName()));

        if (exists) {
            throw new IllegalArgumentException(productModel.getName() + " already exists.");
        }

        return Optional.of(new ProductModel(productRepo.save(new Product(productModel))));
    }

    public void deleteProduct(Long id) {
        if (productRepo.existsById(id)) {
            Product Product = productRepo
                    .findProductByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("Product not found."));

            Product.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            productRepo.save(Product);
        }
    }

    public Optional<ProductModel> updateProduct(ProductModel productModel) {
        boolean exists = productRepo.findProductsWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productModel.getName()));

        if (exists) {
            throw new IllegalArgumentException(productModel.getName() + " already exists.");
        }

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(productModel.getId())
                .orElseThrow(() -> new RuntimeException("This Product does not exist"));

        product.setName(productModel.getName());

        return Optional.of(new ProductModel(productRepo.save(product)));
    }
}