package com.crypto.cryptopricechecker.web.controller;

import com.crypto.cryptopricechecker.service.PriceCheckerService;
import com.crypto.cryptopricechecker.utils.CoinMarketCapClient;
import java.net.HttpRetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/v1/price", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CryptoPriceController {

    @Autowired
    private PriceCheckerService priceCheckerService;

    @GetMapping(value = "/{ticker}")
    public ResponseEntity<String> get(@PathVariable String ticker) throws HttpRetryException {
        ticker = ticker.toUpperCase();
        log.info("Requested data for ticker: " + ticker);

        CoinMarketCapClient coinMarketCapClient = new CoinMarketCapClient();
        coinMarketCapClient.getPrice(ticker);

        var price = priceCheckerService.getPrice(ticker);

        return new ResponseEntity<>(ticker + ": " + price, HttpStatus.OK);
    }

}
