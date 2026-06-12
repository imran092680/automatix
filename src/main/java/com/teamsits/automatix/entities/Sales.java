package com.teamsits.automatix.entities;

import com.teamsits.automatix.entities.common.CommonColumn;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.sales.SalesRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sales extends CommonColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", referencedColumnName = "id", nullable = false)
    private Party party;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sales_id", referencedColumnName = "id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private Double count;

    @Column(nullable = false)
    private Double pricePerUnit;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double discount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    public Sales(
            SalesRequest salesRequest,
            Product product,
            Party party,
            Stock stock
    ) {
        super(
                salesRequest.getCreatedBy(),
                salesRequest.getUpdatedBy()
        );
        this.setProduct(product);
        this.setParty(party);
        this.setStock(stock);
        this.setCount(salesRequest.getCount());
        this.setPricePerUnit(salesRequest.getPricePerUnit());
        this.setAmount(salesRequest.getAmount());
        this.setDiscount(salesRequest.getDiscount());
        this.setTransactionDate(salesRequest.getTransactionDate());
    }
}
