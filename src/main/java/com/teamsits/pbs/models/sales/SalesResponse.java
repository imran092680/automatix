package com.teamsits.pbs.models.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.pbs.entities.Sales;
import com.teamsits.pbs.models.common.CommonModel;
import com.teamsits.pbs.models.master_models.PartyModel;
import com.teamsits.pbs.models.master_models.ProductModel;
import com.teamsits.pbs.models.stock.StockResponse;
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
public class SalesResponse extends CommonModel {
    private ProductModel productModel;
    private PartyModel partyModel;
    private StockResponse stockResponse;
    private Double count;
    private Double pricePerUnit;
    private Double amount;
    private Double discount;
    private LocalDate transactionDate;

    public SalesResponse(Sales sales) {
        super(
                sales.getId(),
                sales.getVersion(),
                sales.getCreatedBy(),
                sales.getCreatedAt(),
                sales.getUpdatedBy(),
                sales.getUpdatedAt()
        );
        this.setProductModel(new ProductModel(sales.getProduct()));
        this.setPartyModel(new PartyModel(sales.getParty()));
        this.setStockResponse(new StockResponse(sales.getStock()));
        this.setCount(sales.getCount());
        this.setPricePerUnit(sales.getPricePerUnit());
        this.setAmount(sales.getAmount());
        this.setDiscount(sales.getDiscount());
        this.setTransactionDate(sales.getTransactionDate());
    }
}
