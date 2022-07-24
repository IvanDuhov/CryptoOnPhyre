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
    private final RequestService requestService;

    // TODO: add third party service to get the rates from / or api endpoint
    //  for providing directly the price from the BTC/fiat pair
    private static final Double USD_EUR_RATE = 1.0004;
    private static final Double USD_BGN_RATE = 1.9872;


    /**
     * Retrieve the cyrpto data from the third party provider
     * @param ticker - the ticker of the crypto
     * @return Coin
     * @throws HttpRetryException - in case something went wrong with the provider
     */
    public Coin getPrice(String ticker) throws HttpRetryException {
        if (!StringValidator.isValidTicker(ticker)) {
            log.error("Sorry, the ticker: {} is in invalid format!", ticker);
            throw new IllegalArgumentException("Sorry, the ticker format is not valid!");
        }

        // Save the request in the DB for further intel
        requestService.saveRequest(ticker);

        return cryptoPriceProvider.getPrice(ticker);
    }

    /**
     * Add all supported fiat pairs. Only a premium feature
     * @param coin - the crypto we want to add the pairs to
     */
    public void addOtherCurrencies(Coin coin) {
        // Add all supported fiat pairs
        coin.getPrice().put("BGN", coin.getPrice().get("USD") * USD_BGN_RATE );
        coin.getPrice().put("EUR", coin.getPrice().get("USD") * USD_EUR_RATE );
    }


}
