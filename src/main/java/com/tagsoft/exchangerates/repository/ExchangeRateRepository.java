package com.tagsoft.exchangerates.repository;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, UUID> {

    @Query("select new com.tagsoft.exchangerates.dto.ExchangeAverageRate(e.targetCurrency, e.baseCurrency, AVG(e.rateBuy), AVG(e.rateSell)) from ExchangeRate e " +
            "WHERE e.dateOfCreate BETWEEN :startDate AND :endDate " +
            "GROUP BY e.targetCurrency, e.baseCurrency " +
            "ORDER BY e.targetCurrency, e.baseCurrency")
    Collection<ExchangeAverageRate> calcAverageExchangeRate(@Param("startDate") Instant startDate, @Param("endDate")Instant endDate);
}
