package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.utils.CryptoPriceProvider;
import com.crypto.cryptopricechecker.utils.StringValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriceCheckerService {

    private final CryptoPriceProvider cryptoPriceProvider;

    public Double getPrice(String ticker) {
        ticker = ticker.toUpperCase();

        if (!StringValidator.isValidTicker(ticker)) {
            throw new IllegalArgumentException("Sorry, the ticker format is not valid!");
        }

        return cryptoPriceProvider.getPrice(ticker);
    }

}
