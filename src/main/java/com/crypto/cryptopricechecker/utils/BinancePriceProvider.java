package com.crypto.cryptopricechecker.utils;

import org.springframework.stereotype.Component;

@Component
public class BinancePriceProvider implements CryptoPriceProvider {

    @Override
    public Double getPrice(String ticker) {
        return 1.2;
    }

}
