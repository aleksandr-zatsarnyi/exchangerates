package com.tagsoft.exchangerates.controller;

import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import com.tagsoft.exchangerates.service.ExchangeRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/rates")
@Tag(name = "Exchange Rates", description = "API for retrieving exchange rate data")
public class ExchangeRatesController {

    private final ExchangeRateService service;

    public ExchangeRatesController(ExchangeRateService service) {
        this.service = service;
    }

    @Operation(summary = "Get average exchange rates for today")
    @GetMapping("/today")
    public Collection<ExchangeAverageRate> getAverageExchangeRatesForToday() {
        return service.getAverageExchangeRatesForToday();
    }

    @Operation(summary = "Get average exchange rates for the specified period")
    @GetMapping("/period")
    public  Collection<ExchangeAverageRate> getAverageExchangeRatesByPeriod(
            @Parameter(description = "Start date of the period", required = true, example = "2023-05-01T00:00:00.000Z") @RequestParam("start") String start,
            @Parameter(description = "End date of the period", required = true, example = "2023-05-10T23:59:59.999Z") @RequestParam("end") String end) {
        return service.getAverageExchangeRatesByPeriod(start, end);
    }
}
