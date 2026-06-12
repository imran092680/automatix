package com.teamsits.automatix.models.receivable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.automatix.entities.Receivable;
import com.teamsits.automatix.models.common.CommonModel;
import com.teamsits.automatix.models.master_models.PartyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceivableResponse extends CommonModel {
    private PartyModel partyModel;
    private Double amount;
    private LocalDate transactionDate;

    public ReceivableResponse(Receivable receivable) {
        super(
                receivable.getId(),
                receivable.getVersion(),
                receivable.getCreatedBy(),
                receivable.getCreatedAt(),
                receivable.getUpdatedBy(),
                receivable.getUpdatedAt()
        );
        this.setPartyModel(receivable.getParty() != null ? new PartyModel(receivable.getParty()) : null);
        this.setAmount(receivable.getAmount());
        this.setTransactionDate(receivable.getTransactionDate());
    }
}
