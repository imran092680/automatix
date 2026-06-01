package com.teamsits.pbs.models.stock;

public interface StockPerProductByDateResponse {
    Long getProductId();

    String getProductName();

    Double getOpeningStock();

    Double getPurchased();

    Double getSold();

    Double getRemainingStock();
}
