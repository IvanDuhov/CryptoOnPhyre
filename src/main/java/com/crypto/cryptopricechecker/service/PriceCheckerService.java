package com.crypto.cryptopricechecker.service;

import com.crypto.cryptopricechecker.integration.CryptoPriceProvider;
import com.crypto.cryptopricechecker.utils.StringValidator;
import com.crypto.cryptopricechecker.web.model.Coin;
import java.net.HttpRetryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PriceCheckerService {

    private final CryptoPriceProvider cryptoPriceProvider;

    public Coin getPrice(String ticker) throws HttpRetryException {
        if (!StringValidator.isValidTicker(ticker)) {
            log.error("Sorry, the ticker: {} is in invalid format!", ticker);
            throw new IllegalArgumentException("Sorry, the ticker format is not valid!");
        }

        return cryptoPriceProvider.getPrice(ticker);
    }

}
