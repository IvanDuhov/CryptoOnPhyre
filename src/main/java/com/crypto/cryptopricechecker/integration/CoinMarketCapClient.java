package com.crypto.cryptopricechecker.integration;

import com.crypto.cryptopricechecker.redis.CacheProvider;
import com.crypto.cryptopricechecker.web.model.Coin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URISyntaxException;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoinMarketCapClient implements CryptoPriceProvider {

    @Value("${cmc.apikey:test}")
    private String apiKey;
    @Value("${cache.expiration:15}000")
    private Integer expirationTimeS;

    @Autowired
    private CacheProvider cacheProvider;

    @Override
    public Coin getPrice(String ticker) throws HttpRetryException {
        try {
            var result = cacheProvider.searchInCache(ticker);

            // If 15s haven't passed since the last cache update, return the value directly
            if (result.getLastUpdated() + expirationTimeS > System.currentTimeMillis()) {
                return result;
            }

            log.info("Cache expired. Updating cache ...");
        } catch (NoSuchElementException e) {
            // Will get that exception in case we don't have that value yet in redis

            // If the cache was populated once, but the coin isn't there
            // then it is not one of the top 200 crypto we are providing
            if (cacheProvider.isCachePopulated()) {
                throw new IllegalSelectorException();
            }

            log.info("Data for {} was request but it wasn't in the cache. Updating cache ...",
                    ticker);
        }

        var availableCoins = getLastPrices();

        // Resetting the cache
        cacheProvider.updateCache(availableCoins.values());

        Coin requestedCoin = availableCoins.get(ticker);

        if (requestedCoin == null) {
            throw new IllegalSelectorException();
        }

        return requestedCoin;
    }

    public Map<String, Coin> getLastPrices() throws HttpRetryException {
        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("start", "1"));
        paratmers.add(new BasicNameValuePair("limit", "200"));

        try {
            String result = makeAPICall(uri, paratmers);
            JsonNode rootNode = new ObjectMapper().readTree(result);

            return getCoinsFromResponse(rootNode);
        } catch (HttpRetryException e) {
            throw new HttpRetryException("The call to the third party provider"
                    + " wasn't successful, please try again",
                    e.responseCode());
        } catch (IOException e) {
            log.error("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            log.error("Error: Invalid URL " + e.toString());
        }

        return null;
    }

    private String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        try (CloseableHttpResponse response = client.execute(request)) {
            System.out.println(response.getStatusLine());

            // Maybe add enhanced exception handling fore support of more status codes
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error(
                        "The call to the third party provider wasn't successful, please try again");
                throw new HttpRetryException(
                        "The call to the third party provider wasn't successful, please try again",
                        response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }

        return response_content;
    }

    private Map<String, Coin> getCoinsFromResponse(JsonNode rawObject) {
        Map<String, Coin> coins = new HashMap<>();

        for (var ticker : rawObject.get("data")) {
            String symbol = ticker.get("symbol").toString().replace("\"", "");

            coins.put(symbol,
                    new Coin(symbol, Double.parseDouble(
                            ticker.get("quote").get("USD").get("price").toString()),
                            System.currentTimeMillis()));
        }

        return coins;
    }

}
