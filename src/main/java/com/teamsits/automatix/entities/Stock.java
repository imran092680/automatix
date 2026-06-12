package com.teamsits.automatix.entities;

import com.teamsits.automatix.entities.common.TenantAuditBase;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.enums.StockTransactionType;
import com.teamsits.automatix.models.cash_out.CashOutRequest;
import com.teamsits.automatix.models.sales.SalesRequest;
import com.teamsits.automatix.models.stock.StockRequest;
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
public class Stock extends TenantAuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockTransactionType stockTransactionType;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private LocalDate transactionDate;

    public Stock(Product product, StockRequest stockRequest) {
        super(
                stockRequest.getCreatedBy(),
                stockRequest.getUpdatedBy()
        );
        this.setProduct(product);
        this.setStockTransactionType(StockTransactionType.PURCHASE);
        this.setQuantity(stockRequest.getQuantity());
        this.setTransactionDate(stockRequest.getTransactionDate());
    }

    public Stock(Product product, SalesRequest salesRequest) {
        super(
                salesRequest.getCreatedBy(),
                salesRequest.getUpdatedBy()
        );
        this.setProduct(product);
        this.setStockTransactionType(StockTransactionType.SALES);
        this.setQuantity(salesRequest.getCount());
        this.setTransactionDate(salesRequest.getTransactionDate());
    }

    public Stock(Product product, CashOutRequest cashOutRequest) {
        super(
                cashOutRequest.getCreatedBy(),
                cashOutRequest.getUpdatedBy()
        );
        this.setProduct(product);
        this.setStockTransactionType(StockTransactionType.PURCHASE);
        this.setQuantity(cashOutRequest.getQuantity());
        this.setTransactionDate(cashOutRequest.getTransactionDate());
    }
}
