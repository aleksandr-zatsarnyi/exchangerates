package com.tagsoft.exchangerates.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NationalBankExchangeRateDto {

    private String txt;
    private Double rate;
    private String cc;
    private String exchangedate;
}
