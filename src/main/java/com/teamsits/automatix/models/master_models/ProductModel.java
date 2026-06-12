package com.teamsits.automatix.models.master_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.common.CommonModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductModel extends CommonModel {
    String name;

    public ProductModel(Product product) {
        super(
                product.getId(),
                product.getVersion(),
                product.getCreatedBy(),
                product.getCreatedAt(),
                product.getUpdatedBy(),
                product.getUpdatedAt()
        );
        this.setName(product.getName());
    }
}
