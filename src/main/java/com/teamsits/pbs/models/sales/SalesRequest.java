package com.teamsits.pbs.models.sales;

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
public class SalesRequest extends CommonModel {
    private Long productId;
    private Long partyId;
    private Long stockId;
    private Double count;
    private Double pricePerUnit;
    private Double amount;
    private Double discount;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;
}
