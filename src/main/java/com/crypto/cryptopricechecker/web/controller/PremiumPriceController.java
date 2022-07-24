package com.crypto.cryptopricechecker.web.controller;

import com.crypto.cryptopricechecker.service.PriceCheckerService;
import com.crypto.cryptopricechecker.web.model.Coin;
import java.net.HttpRetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
@Slf4j
public class PremiumPriceController {

    @Autowired
    private PriceCheckerService priceCheckerService;

    @GetMapping(value = "/price/{ticker}")
    public ResponseEntity<Coin> get(@PathVariable String ticker) throws HttpRetryException {
        ticker = ticker.toUpperCase();
        log.info("Requested data for ticker: " + ticker);

        var res = priceCheckerService.getPrice(ticker);
        priceCheckerService.addOtherCurrencies(res);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
