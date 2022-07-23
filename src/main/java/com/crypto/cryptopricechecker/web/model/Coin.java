package com.crypto.cryptopricechecker.web.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "Coin")
public class Coin implements Serializable {
    @Id
    private String ticker;
    private Double price;
}
