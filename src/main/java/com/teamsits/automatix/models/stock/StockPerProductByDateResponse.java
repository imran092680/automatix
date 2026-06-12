package com.teamsits.automatix.models.stock;

public interface StockPerProductByDateResponse {
    Long getProductId();

    String getProductName();

    Double getOpeningStock();

    Double getPurchased();

    Double getSold();

    Double getRemainingStock();
}
