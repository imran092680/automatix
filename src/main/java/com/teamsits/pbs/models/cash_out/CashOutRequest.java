package com.teamsits.pbs.models.cash_out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashOutRequest extends CommonModel {
    private Long productId;
    private Long partyId;
    private Long bankId;
    private Double quantity;
    private String particulars; // reason in frontend
    private Double amount;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;
    private Long stockId;
}
