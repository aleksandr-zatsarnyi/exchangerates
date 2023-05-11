package com.tagsoft.exchangerates.service.privatebank;

import com.tagsoft.exchangerates.domain.ExchangeRate;
import com.tagsoft.exchangerates.dto.PrivateBankExchangeRateDto;
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
public class PrivateBankProvider implements ExchangeRateProvider {

    private final RestTemplate restTemplate;
    private final TranslationService translationService;

    public PrivateBankProvider(TranslationService translationService, RestTemplate restTemplate) {
        this.translationService = translationService;
        this.restTemplate = restTemplate;
    }

    public List<ExchangeRate> getExchangeRates() {
        String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";
        try {
            ResponseEntity<List<PrivateBankExchangeRateDto>> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            return Optional.ofNullable(responseEntity.getBody())
                    .orElseThrow(()-> new DataUnavailableException(PrivateBankProvider.class.getSimpleName()))
                    .stream()
                    .filter(e -> e.getBuy() > 0 && e.getSale() > 0)
                    .map(translationService::toEntity).toList();
        } catch (RestClientException e) {
            throw new DataUnavailableException(PrivateBankProvider.class.getSimpleName(), e.getMessage());
        }
    }
}
