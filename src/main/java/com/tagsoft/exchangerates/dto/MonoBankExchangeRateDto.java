package com.tagsoft.exchangerates.dto;

import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
public class MonoBankExchangeRateDto {
    private String currencyCodeA;
    private String currencyCodeB;
    private Instant date;
    private Double rateBuy;
    private Double rateCross;
    private Double rateSell;
}
