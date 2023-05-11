package com.tagsoft.exchangerates.service;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.MonoBankExchangeRateDto;
import com.tagsoft.exchangerates.dto.NationalBankExchangeRateDto;
import com.tagsoft.exchangerates.dto.PrivateBankExchangeRateDto;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Optional;

@Service
public class TranslationService {

    public ExchangeRate toEntity(MonoBankExchangeRateDto dto) {
        return toEntity(getCurrencyLetterCode(dto.getCurrencyCodeA()),
                getCurrencyLetterCode(dto.getCurrencyCodeB()), dto.getRateBuy(), dto.getRateSell());
    }

    public ExchangeRate toEntity(PrivateBankExchangeRateDto dto) {
        return toEntity(dto.getCcy(), dto.getBase_ccy(), dto.getBuy(), dto.getSale());
    }

    public ExchangeRate toEntity(NationalBankExchangeRateDto dto) {
        return toEntity(dto.getCc(), "UAH", dto.getRate(), null);
    }

    private ExchangeRate toEntity(String targetCurrencyCode, String baseCurrencyCode, Double rateBuy, Double rateSell) {
        ExchangeRate rate = new ExchangeRate();
        rate.setTargetCurrency(targetCurrencyCode);
        rate.setBaseCurrency(baseCurrencyCode);
        rate.setRateBuy(rateBuy);
        rate.setRateSell(rateSell);

        return rate;
    }

    private String getCurrencyLetterCode(String code) {
        try {
            Optional<Currency> currency =
                    Currency.getAvailableCurrencies().stream().filter(c -> c.getNumericCode() == Integer.parseInt(code)).findAny();

            if (currency.isPresent()) {
                return currency.get().getCurrencyCode();
            }
        } catch (IllegalArgumentException e) {
            return code;
        }
        return code;
    }
}
