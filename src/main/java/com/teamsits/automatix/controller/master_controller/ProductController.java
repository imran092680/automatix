package com.teamsits.automatix.controller.master_controller;

import com.teamsits.automatix.models.master_models.ProductModel;
import com.teamsits.automatix.service.master_data_service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/product")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductModel>> getProducts() {
        try {
            return ResponseEntity.ok(productService.getProducts());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<ProductModel>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<ProductModel>> addProduct(@RequestBody ProductModel ProductModel) {
        return ResponseEntity.ok(productService.addProduct(ProductModel));
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<ProductModel>> updateProduct(@RequestBody ProductModel productModel) {
        return ResponseEntity.ok(productService.updateProduct(productModel));
    }
}

