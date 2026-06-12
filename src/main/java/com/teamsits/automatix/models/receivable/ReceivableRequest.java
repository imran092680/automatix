package com.teamsits.automatix.models.receivable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.automatix.models.common.CommonModel;
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
public class ReceivableRequest extends CommonModel {
    private Long partyId;
    private Double amount;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;
}
