package com.tagsoft.exchangerates.service.monobank;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.MonoBankExchangeRateDto;
import com.tagsoft.exchangerates.exception.DataUnavailableException;
import com.tagsoft.exchangerates.service.ExchangeRateProvider;
import com.tagsoft.exchangerates.service.TranslationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class MonoBankProvider implements ExchangeRateProvider {

    private final TranslationService translationService;
    private final RestTemplate restTemplate;

    public MonoBankProvider(TranslationService translationService, RestTemplate restTemplate) {
        this.translationService = translationService;
        this.restTemplate = restTemplate;
    }

    public List<ExchangeRate> getExchangeRates() {
        String url = "https://api.monobank.ua/bank/currency";
        try {
        ResponseEntity<List<MonoBankExchangeRateDto>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        return Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(()-> new DataUnavailableException(MonoBankProvider.class.getSimpleName()))
                .stream()
                .filter(e -> e.getRateBuy() > 0 && e.getRateSell() > 0)
                .map(translationService::toEntity).toList();
        } catch (RestClientException e) {
            throw new DataUnavailableException(MonoBankProvider.class.getSimpleName(), e.getMessage());
        }
    }
}
