package com.teamsits.automatix.entities;

import com.teamsits.automatix.entities.common.TenantAuditBase;
import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.cash_out.CashOutRequest;
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
public class CashOut extends TenantAuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    private Party party;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private Bank bank;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    private Double quantity;
    private String particulars; // reason or purpose in frontend

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    public CashOut(
            CashOutRequest cashOutRequest,
            Product product,
            Party party,
            Bank bank,
            Stock stock
    ) {
        super(
                cashOutRequest.getCreatedBy(),
                cashOutRequest.getUpdatedBy()
        );
        this.setProduct(product);
        this.setParty(party);
        this.setBank(bank);
        this.setQuantity(cashOutRequest.getQuantity());
        this.setParticulars(cashOutRequest.getParticulars());
        this.setAmount(cashOutRequest.getAmount());
        this.setTransactionDate(cashOutRequest.getTransactionDate());
        this.setStock(stock);
    }
}
