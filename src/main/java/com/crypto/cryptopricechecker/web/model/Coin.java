package com.crypto.cryptopricechecker.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coin {
    private final String ticker;
    private final Double price;
}
