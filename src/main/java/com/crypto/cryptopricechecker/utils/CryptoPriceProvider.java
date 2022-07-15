package com.crypto.cryptopricechecker.utils;

public interface CryptoPriceProvider {

    Double getPrice(String ticker);

}
