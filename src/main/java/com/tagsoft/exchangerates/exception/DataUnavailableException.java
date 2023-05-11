package com.tagsoft.exchangerates.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataUnavailableException extends RuntimeException {

    public DataUnavailableException(String provider) {
        super(String.format("Can't connect to the bankApi %s ", provider));
    }

    public DataUnavailableException(String provider, String message) {
        super(String.format("Can't connect to the bankApi %s, error message : %s", provider, message));
    }
}
