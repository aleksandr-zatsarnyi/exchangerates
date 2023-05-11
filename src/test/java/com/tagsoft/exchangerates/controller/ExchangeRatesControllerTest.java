package com.tagsoft.exchangerates.controller;

import com.tagsoft.exchangerates.dto.ExchangeAverageRate;
import com.tagsoft.exchangerates.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ExchangeRatesController.class)
public class ExchangeRatesControllerTest {

    @MockBean
    private ExchangeRateService service;

    @Autowired
    protected MockMvc mvc;

    @Test
    public void getAverageExchangeRatesForToday() throws Exception {
        List<ExchangeAverageRate> exchangeAverageRates = new ArrayList<>();
        ExchangeAverageRate exchangeAverageRate = new ExchangeAverageRate("USD", "EUR", 1.2, 2.2);
        exchangeAverageRates.add(exchangeAverageRate);

        Mockito.when(service.getAverageExchangeRatesForToday()).thenReturn(exchangeAverageRates);

        mvc.perform(get("/api/v1/rates/today"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].baseCurrency").value("EUR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetCurrency").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].averageRateBuy").value(1.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].averageRateSell").value(2.2));
    }

    @Test
    public void testGetAverageExchangeRatesByPeriod() throws Exception {
        List<ExchangeAverageRate> exchangeAverageRates = new ArrayList<>();
        ExchangeAverageRate exchangeAverageRate = new ExchangeAverageRate("USD", "EUR", 1.2, 2.2);
        exchangeAverageRates.add(exchangeAverageRate);

        Mockito.when(service.getAverageExchangeRatesByPeriod(anyString(), anyString())).thenReturn(exchangeAverageRates);

        mvc.perform(get("/api/v1/rates/period")
                        .param("start", "2023-05-01T00:00:00.000Z")
                        .param("end", "2023-05-10T23:59:59.999Z")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].baseCurrency").value("EUR"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetCurrency").value("USD"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].averageRateBuy").value(1.2))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].averageRateSell").value(2.2));
    }
}
