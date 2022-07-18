package com.crypto.cryptopricechecker.utils;

import java.net.HttpRetryException;

public interface CryptoPriceProvider {

    Double getPrice(String ticker) throws HttpRetryException;

}
