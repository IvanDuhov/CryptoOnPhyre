package com.crypto.cryptopricechecker.redis;

import com.crypto.cryptopricechecker.redis.repository.CoinRepository;
import com.crypto.cryptopricechecker.web.model.Coin;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheProvider {

    @Autowired
    private CoinRepository coinRepository;

    private boolean cachePopulated = false;

    public Coin searchInCache(String ticker) {
        var res = coinRepository.findById(ticker);

        if (res.isPresent()) {
            return res.get();
        }

        throw new NoSuchElementException();
    }

    public void updateCache(Iterable<Coin> coins) {
        coinRepository.deleteAll();
        coinRepository.saveAll(coins);
        cachePopulated = true;
        log.info("Cache updated successfully");
    }

    public boolean isCachePopulated() {
        return cachePopulated;
    }

}
