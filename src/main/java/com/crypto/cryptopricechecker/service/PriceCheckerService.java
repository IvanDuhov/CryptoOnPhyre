package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.utils.CryptoPriceProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriceCheckerService {

    private final CryptoPriceProvider cryptoPriceProvider;

    public Double getPrice(String ticker) {
        return cryptoPriceProvider.getPrice(ticker);
    }

}
