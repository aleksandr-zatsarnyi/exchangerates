package com.tagsoft.exchangerates.job;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.service.ExchangeRateProvider;
import com.tagsoft.exchangerates.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class LoadExchangeRatesJob {

    private final ExchangeRateService service;

    private final List<ExchangeRateProvider> providers;

    @Autowired
    public LoadExchangeRatesJob(ExchangeRateService service, List<ExchangeRateProvider> providers) {
        this.service = service;
        this.providers = providers;
    }

    @Transactional
    @Scheduled(cron = "${load.exchange.rates.job.cron}")
    public void loadExchangeRates() {
        log.info("Job started");
        List<ExchangeRate> allExchangeRates = providers.stream()
                .flatMap(provider -> provider.getExchangeRates().stream()).toList();
        if (!allExchangeRates.isEmpty()) {
            service.saveExchangeRates(allExchangeRates);
            log.info("Job finished");
        } else {
            log.warn("No exchange rates to download");
        }
    }
}
