package com.crypto.cryptopricechecker.redis.repository;

import com.crypto.cryptopricechecker.web.model.Coin;
import org.springframework.data.repository.CrudRepository;

public interface CoinRepository extends CrudRepository<Coin, String> {
}
