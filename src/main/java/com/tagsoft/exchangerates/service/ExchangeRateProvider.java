package com.tagsoft.exchangerates.service;

import com.tagsoft.exchangerates.domain.ExchangeRate;

import java.util.List;

public interface ExchangeRateProvider {

    List<ExchangeRate> getExchangeRates();
}
