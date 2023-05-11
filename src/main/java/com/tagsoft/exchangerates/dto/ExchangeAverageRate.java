package com.tagsoft.exchangerates.dto;

import lombok.*;

@Data
public class ExchangeAverageRate {

    private String targetCurrency;
    private String baseCurrency;
    private Double averageRateBuy;
    private Double averageRateSell;

    public ExchangeAverageRate(String targetCurrency, String baseCurrency, Double averageRateBuy, Double averageRateSell) {
        this.targetCurrency = targetCurrency;
        this.baseCurrency = baseCurrency;
        this.averageRateBuy = averageRateBuy;
        this.averageRateSell = averageRateSell;
    }
}
