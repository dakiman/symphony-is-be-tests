package com.daki.apitesting.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class BaseClient {

    protected abstract String getBaseUrl();
//
//    protected T executeRequest(HttpMethod httpMethod, String path, Object body, T responseType) {
////        https://my-json-server.typicode.com/dakiman/json-server/posts
//
//        WebClient client = WebClient
//                .builder()
//                .baseUrl(getBaseUrl())
//                .build();
//
//        String response = client
//                .method(httpMethod)
//                .uri(path)
//                .body(BodyInserters.fromValue(body))
//                .exchangeToMono(response -> response.bodyToMono(String.class))
//                .block();
//
//        return response;
//    }

    protected <T> T executeRequest(HttpMethod httpMethod, String path, Class<T> responseType) {
//        https://my-json-server.typicode.com/dakiman/json-server/posts
        WebClient client = WebClient
                .builder()
                .baseUrl(getBaseUrl())
                .build();

        return client
                .method(httpMethod)
                .uri(path)
                .exchangeToMono(response -> response.bodyToMono(responseType))
                .block();


    }
}
