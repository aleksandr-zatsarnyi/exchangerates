package com.tagsoft.exchangerates.service;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import com.tagsoft.exchangerates.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;

    public ExchangeRateService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    public Collection<ExchangeAverageRate> getAverageExchangeRatesForToday() {
        return repository.calcAverageExchangeRate(getStartOfToday(), getEndOfToday());

    }

    public Collection<ExchangeAverageRate> getAverageExchangeRatesByPeriod(String start, String end) {
        Instant startDate;
        Instant endDate;
        try {
            startDate = Instant.parse(start);
            endDate = Instant.parse(end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", e);
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must not be before start date");
        }

        return repository.calcAverageExchangeRate(startDate, endDate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveExchangeRates(Collection<ExchangeRate> rateList) {
        repository.saveAll(rateList);
    }

    private Instant getStartOfToday() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }

    private Instant getEndOfToday() {
        return getStartOfToday().plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
    }
}
