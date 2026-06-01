package com.teamsits.pbs.models.stock;

import com.teamsits.pbs.enums.StockTransactionType;
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
public class StockRequest extends CommonModel {
    private Long productId;
    private StockTransactionType stockTransactionType;
    private Double quantity;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;
}
