package com.tagsoft.exchangerates.repository;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import com.tagsoft.exchangerates.factory.ExchangeRateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(statements = {"DELETE FROM exchange_rate"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ExchangeRateRepositoryTest {

    @Autowired
    private ExchangeRateRepository repository;

    @Autowired
    private ExchangeRateFactory factory;

    @Test
    public void testSaveExchangeRate() {
        ExchangeRate rate = factory.createExchangeRate();

        ExchangeRate savedRate = repository.save(rate);
        Assertions.assertNotNull(savedRate.getId());
        Assertions.assertTrue(repository.findById(savedRate.getId()).isPresent());
    }

    @Test
    public void testCalcAverageExchangeRateByDate() {
        ExchangeRate firstEntity = factory.createExchangeRate("UAH", "USD", "2023-05-10 10:00:00");
        repository.save(firstEntity);
        ExchangeRate secondEntity = factory.createExchangeRate("UAH", "USD", "2023-05-10 12:30:00");
        repository.save(secondEntity);
        ExchangeRate thirdEntity = factory.createExchangeRate("UAH", "USD", "2023-05-10 13:30:00");
        repository.save(thirdEntity);
        ExchangeRate fourthEntity = factory.createExchangeRate("UAH", "EUR", "2023-05-10 13:50:00");
        repository.save(fourthEntity);
        ExchangeRate fivesEntity = factory.createExchangeRate("UAH", "EUR", "2023-05-10 14:30:00");
        repository.save(fivesEntity);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-05-10 13:00:00", formatter);
        Instant startDate = localStartDateTime.toInstant(ZoneOffset.UTC);
        LocalDateTime localEndDateTime = LocalDateTime.parse("2023-05-10 15:00:00", formatter);
        Instant endDate = localEndDateTime.toInstant(ZoneOffset.UTC);

        double calcAverageBuyRateForEur = (fourthEntity.getRateBuy() + fivesEntity.getRateBuy()) / 2.0;
        double calcAverageSellRateForEur = (fourthEntity.getRateSell() + fivesEntity.getRateSell()) / 2.0;

        ArrayList<ExchangeAverageRate> exchangeAverageList = new ArrayList<>(repository.calcAverageExchangeRate(startDate, endDate));

        for (ExchangeAverageRate rate : exchangeAverageList) {
            System.out.println(rate.toString());
            if (rate.getBaseCurrency().equals("UAH") && rate.getTargetCurrency().equals("USD")) {
                Assertions.assertEquals(thirdEntity.getRateBuy(), rate.getAverageRateBuy());
                Assertions.assertEquals(thirdEntity.getRateSell(), rate.getAverageRateSell());
            }
            if (rate.getBaseCurrency().equals("UAH") && rate.getTargetCurrency().equals("EUR")) {
                Assertions.assertEquals(calcAverageBuyRateForEur, rate.getAverageRateBuy());
                Assertions.assertEquals(calcAverageSellRateForEur, rate.getAverageRateSell());
            }
        }
    }
}
