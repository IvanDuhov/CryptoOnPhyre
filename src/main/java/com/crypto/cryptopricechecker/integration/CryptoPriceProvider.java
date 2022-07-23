package com.crypto.cryptopricechecker.integration;

import com.crypto.cryptopricechecker.web.model.Coin;
import java.net.HttpRetryException;

public interface CryptoPriceProvider {

    Coin getPrice(String ticker) throws HttpRetryException;

}
