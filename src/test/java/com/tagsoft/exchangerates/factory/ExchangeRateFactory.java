package com.tagsoft.exchangerates.factory;

import com.github.javafaker.Faker;
import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ExchangeRateFactory {

    @Autowired
    private Faker faker;

    public ExchangeRate createExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setTargetCurrency(faker.currency().code());
        exchangeRate.setBaseCurrency(faker.currency().code());
        exchangeRate.setRateBuy(faker.number().randomDouble(4, 0, 1000));
        exchangeRate.setRateSell(faker.number().randomDouble(4, 0, 1000));
        exchangeRate.setDateOfCreate(Instant.now());
        return exchangeRate;
    }

    public ExchangeRate createExchangeRate(String baseCurrency, String targetCurrency) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setRateBuy(faker.number().randomDouble(4, 0, 1000));
        exchangeRate.setRateSell(faker.number().randomDouble(4, 0, 1000));
        exchangeRate.setDateOfCreate(Instant.now());
        return exchangeRate;
    }

    public ExchangeRate createExchangeRate(String baseCurrency, String targetCurrency, String dateTime) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setRateBuy(faker.number().randomDouble(4, 0, 1000));
        exchangeRate.setRateSell(faker.number().randomDouble(4, 0, 1000));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        exchangeRate.setDateOfCreate(instant);

        return exchangeRate;
    }

    public Collection<ExchangeAverageRate> createExchangeAverageRate() {
        Collection<ExchangeAverageRate> exchangeAverageRates = new ArrayList<>();
        exchangeAverageRates.add(new ExchangeAverageRate(
                faker.currency().code(),
                faker.currency().code(),
                faker.number().randomDouble(4, 0, 1000),
                faker.number().randomDouble(4, 0, 1000)));
        exchangeAverageRates.add(new ExchangeAverageRate(
                faker.currency().code(),
                faker.currency().code(), faker.number().
                randomDouble(4, 0, 1000),
                faker.number().randomDouble(4, 0, 1000)));
        exchangeAverageRates.add(new ExchangeAverageRate(faker.currency().code(),
                faker.currency().code(),
                faker.number().randomDouble(4, 0, 1000),
                faker.number().randomDouble(4, 0, 1000)));

        return exchangeAverageRates;
    }
}
