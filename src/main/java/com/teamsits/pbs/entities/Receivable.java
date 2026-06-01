package com.teamsits.pbs.entities;

import com.teamsits.pbs.entities.common.CommonColumn;
import com.teamsits.pbs.entities.master_entity.Party;
import com.teamsits.pbs.models.receivable.ReceivableRequest;
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
public class Receivable extends CommonColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    private Party party;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    public Receivable(ReceivableRequest receivableRequest, Party party) {
        super(
                receivableRequest.getCreatedBy(),
                receivableRequest.getUpdatedBy()
        );
        this.setParty(party);
        this.setAmount(receivableRequest.getAmount());
        this.setTransactionDate(receivableRequest.getTransactionDate());
    }
}
