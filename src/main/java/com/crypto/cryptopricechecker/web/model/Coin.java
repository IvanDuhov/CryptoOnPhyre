package com.crypto.cryptopricechecker.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Dictionary;
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
    private Dictionary<String, Double> price;
    @JsonIgnore
    private long lastUpdated;
}
