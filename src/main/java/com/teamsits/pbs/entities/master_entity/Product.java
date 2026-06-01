package com.teamsits.pbs.entities.master_entity;

import com.teamsits.pbs.entities.common.CommonColumn;
import com.teamsits.pbs.models.master_models.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends CommonColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Product(ProductModel productModel) {
        super(
                productModel.getCreatedBy(),
                productModel.getUpdatedBy()
        );
        this.setName(productModel.getName());
    }
}
