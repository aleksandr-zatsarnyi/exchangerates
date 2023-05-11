package com.tagsoft.exchangerates.service.nationbank;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.NationalBankExchangeRateDto;
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
public class NationalBankProvider implements ExchangeRateProvider {

    private final TranslationService translationService;
    private final RestTemplate restTemplate;

    public NationalBankProvider(TranslationService translationService, RestTemplate restTemplate) {
        this.translationService = translationService;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ExchangeRate> getExchangeRates() {
        String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
        try {
            ResponseEntity<List<NationalBankExchangeRateDto>> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            return Optional.ofNullable(responseEntity.getBody())
                    .orElseThrow(()-> new DataUnavailableException(NationalBankProvider.class.getSimpleName()))
                    .stream()
                    .filter(e -> e.getRate() > 0)
                    .map(translationService::toEntity).toList();
        } catch (RestClientException e) {
            throw new DataUnavailableException(NationalBankProvider.class.getSimpleName(), e.getMessage());
        }
    }
}
