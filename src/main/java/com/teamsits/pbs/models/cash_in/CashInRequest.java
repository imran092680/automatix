package com.teamsits.pbs.models.cash_in;

import com.teamsits.pbs.entities.CashIn;
import com.teamsits.pbs.models.common.CommonModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CashInRequest extends CommonModel {
    private Long partyId;
    private Long bankId;
    private String purpose;
    private Double amount;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate memoDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;

    public CashInRequest(CashIn cashIn) {
        super(
                cashIn.getId(),
                cashIn.getVersion(),
                cashIn.getCreatedBy(),
                cashIn.getCreatedAt(),
                cashIn.getUpdatedBy(),
                cashIn.getUpdatedAt()
        );
        this.setPartyId(cashIn.getParty().getId());
        this.setBankId(cashIn.getBank().getId());
        this.setPurpose(cashIn.getPurpose());
        this.setAmount(cashIn.getAmount());
        this.setMemoDate(cashIn.getMemoDate());
    }
}
