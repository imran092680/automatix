package com.teamsits.pbs.models.cash_in;

import com.teamsits.pbs.entities.CashIn;
import com.teamsits.pbs.models.common.CommonModel;
import com.teamsits.pbs.models.master_models.BankModel;
import com.teamsits.pbs.models.master_models.PartyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CashInResponse extends CommonModel {
    private PartyModel partyModel;
    private BankModel bankModel;
    private String purpose;
    private Double amount;
    private LocalDate memoDate;
    private LocalDate transactionDate;

    public CashInResponse(CashIn cashIn) {
        super(
                cashIn.getId(),
                cashIn.getVersion(),
                cashIn.getCreatedBy(),
                cashIn.getCreatedAt(),
                cashIn.getUpdatedBy(),
                cashIn.getUpdatedAt()
        );

        this.setPartyModel(cashIn.getParty() != null ? new PartyModel(cashIn.getParty()) : null);
        this.setBankModel(cashIn.getBank() != null ? new BankModel(cashIn.getBank()) : null);
        this.setPurpose(cashIn.getPurpose());
        this.setAmount(cashIn.getAmount());
        this.setMemoDate(cashIn.getMemoDate());
        this.setTransactionDate(cashIn.getTransactionDate());
    }
}