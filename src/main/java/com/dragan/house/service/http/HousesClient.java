package com.dragan.house.service.http;

import com.dragan.house.service.model.House;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HousesClient extends BaseClient {

    @Value("${services.house-service.url}")
    private String baseUrl;

    protected String getBaseUrl() {
        return baseUrl;
    }

    public ResponseEntity<List<House>> getHouses(String priceGte, String priceLte, String city) {
//        ?price_gte=&price_lte=&city=Austin
        return executeRequest(HttpMethod.GET, "/houses", new ParameterizedTypeReference<List<House>>() {
        }, null, Map.of(
                "price_gte", priceGte,
                "price_lte", priceLte,
                "city", city
        ));
    }
}
