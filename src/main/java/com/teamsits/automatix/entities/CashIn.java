package com.teamsits.automatix.entities;

import com.teamsits.automatix.entities.common.CommonColumn;
import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.models.cash_in.CashInRequest;
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
public class CashIn extends CommonColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    private Party party;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private Bank bank;

    private String purpose;

    private Double amount;
    private LocalDate memoDate;
    private LocalDate transactionDate;

//    @PrePersist
//    protected void onCreate() {
//        // Automatically set createdAt to the current date
//        this.transactionDate = LocalDate.now();
//    }

    public CashIn(CashInRequest cashInRequest, Party party, Bank bank) {
        super(
                cashInRequest.getCreatedBy(),
                cashInRequest.getUpdatedBy()
        );
        this.setParty(party);
        this.setBank(bank);
        this.setPurpose(cashInRequest.getPurpose());
        this.setAmount(cashInRequest.getAmount());
        this.setMemoDate(cashInRequest.getMemoDate());
        this.setTransactionDate(cashInRequest.getTransactionDate());
    }
}
