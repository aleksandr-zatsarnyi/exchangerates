package com.tagsoft.exchangerates.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRate {

    @Id
    @GeneratedValue
    private UUID id;

    private String targetCurrency;
    private String baseCurrency;
    private Double rateBuy;
    private Double rateSell;

    @CreatedDate
    private Instant dateOfCreate;
}
