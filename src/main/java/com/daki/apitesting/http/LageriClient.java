package com.daki.apitesting.http;

import com.daki.apitesting.model.Item;
import com.daki.apitesting.model.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LageriClient extends BaseClient {

    @Value("${services.lageri.url}")
    private String baseUrl;

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    public ResponseEntity<Item[]> getItems() {
        return executeRequest(HttpMethod.GET, "/test", Item[].class);
    }

}
