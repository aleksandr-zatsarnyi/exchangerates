package com.tagsoft.exchangerates.service;

import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import com.tagsoft.exchangerates.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService service;

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    public void testGetAverageExchangeRatesForToday() {
        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS);
        List<ExchangeAverageRate> expectedRates = new ArrayList<>();
        ExchangeAverageRate expectedRate = new ExchangeAverageRate("USD", "UAH", 1.0, 2.0);
        expectedRates.add(expectedRate);
        when(exchangeRateRepository.calcAverageExchangeRate(start, end)).thenReturn(expectedRates);

        Collection<ExchangeAverageRate> actualRates = service.getAverageExchangeRatesForToday();

        Assertions.assertEquals(expectedRates, actualRates);
        Mockito.verify(exchangeRateRepository).calcAverageExchangeRate(start, end);
    }

    @Test
    public void testGetAverageExchangeRatesByPeriod() {
        String start = "2023-05-01T00:00:00.000Z";
        String end = "2023-05-10T23:59:59.999Z";
        Instant startDate = Instant.parse(start);
        Instant endDate = Instant.parse(end);
        List<ExchangeAverageRate> expectedRates = new ArrayList<>();
        ExchangeAverageRate expectedRate = new ExchangeAverageRate("USD", "UAH", 1.0, 2.0);
        expectedRates.add(expectedRate);
        when(exchangeRateRepository.calcAverageExchangeRate(startDate, endDate)).thenReturn(expectedRates);

        Collection<ExchangeAverageRate> actualRates = service.getAverageExchangeRatesByPeriod(start, end);

        Assertions.assertEquals(expectedRates, actualRates);
        Mockito.verify(exchangeRateRepository).calcAverageExchangeRate(startDate, endDate);
    }

    @Test
    public void testGetAverageExchangeRatesByPeriodThrowsExceptionOnInvalidDateFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.getAverageExchangeRatesByPeriod("2023-05-01", "2023-05-10"));
    }

    @Test
    public void testGetAverageExchangeRatesByPeriodThrowsExceptionOnEndDateBeforeStartDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.getAverageExchangeRatesByPeriod("2023-05-10T00:00:00.000Z", "2023-05-01T23:59.000Z"));
    }
}
