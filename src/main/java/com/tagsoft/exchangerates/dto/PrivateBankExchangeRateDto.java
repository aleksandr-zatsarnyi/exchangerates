package com.tagsoft.exchangerates.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PrivateBankExchangeRateDto {

    private String ccy;
    private String base_ccy;
    private Double buy;
    private Double sale;
}
