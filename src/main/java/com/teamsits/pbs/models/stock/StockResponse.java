package com.teamsits.pbs.models.stock;

import com.teamsits.pbs.entities.Stock;
import com.teamsits.pbs.models.master_models.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private ProductModel productModel;
    private Double quantity;
    private LocalDate transactionDate;

    public StockResponse(Stock stock) {
        this.setProductModel(new ProductModel(stock.getProduct()));
        this.setQuantity(stock.getQuantity());
        this.setTransactionDate(stock.getTransactionDate());
    }
}
