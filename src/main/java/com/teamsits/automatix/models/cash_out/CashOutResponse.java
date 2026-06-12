package com.teamsits.automatix.models.cash_out;

import com.teamsits.automatix.entities.CashOut;
import com.teamsits.automatix.models.common.CommonModel;
import com.teamsits.automatix.models.master_models.BankModel;
import com.teamsits.automatix.models.master_models.PartyModel;
import com.teamsits.automatix.models.master_models.ProductModel;
import com.teamsits.automatix.models.stock.StockResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashOutResponse extends CommonModel {
    private ProductModel productModel;
    private PartyModel partyModel;
    private BankModel bankModel;
    private Double quantity;
    private String particulars; // reason or purpose in frontend
    private Double amount;
    private LocalDate transactionDate;
    private StockResponse stockResponse;

    public CashOutResponse(CashOut cashOut) {
        super(
                cashOut.getId(),
                cashOut.getVersion(),
                cashOut.getCreatedBy(),
                cashOut.getCreatedAt(),
                cashOut.getUpdatedBy(),
                cashOut.getUpdatedAt()
        );
        this.setProductModel(cashOut.getProduct() != null ? new ProductModel(cashOut.getProduct()) : null);
        this.setPartyModel(cashOut.getParty() != null ? new PartyModel(cashOut.getParty()) : null);
        this.setBankModel(cashOut.getBank() != null ? new BankModel(cashOut.getBank()) : null);
        this.setQuantity(cashOut.getQuantity());
        this.setParticulars(cashOut.getParticulars());
        this.setAmount(cashOut.getAmount());
        this.setTransactionDate(cashOut.getTransactionDate());
        this.setStockResponse(cashOut.getStock() != null ? new StockResponse(cashOut.getStock()) : null);
    }
}
