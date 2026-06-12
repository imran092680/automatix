package com.teamsits.automatix.entities.master_entity;

import com.teamsits.automatix.entities.common.TenantAuditBase;
import com.teamsits.automatix.models.master_models.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends TenantAuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Product(ProductModel productModel) {
        super(
                productModel.getCreatedBy(),
                productModel.getUpdatedBy()
        );
        this.setName(productModel.getName());
    }
}
