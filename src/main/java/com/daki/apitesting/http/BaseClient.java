package com.daki.apitesting.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public abstract class BaseClient {

    protected abstract String getBaseUrl();

    protected <T> ResponseEntity<T> executeRequest(HttpMethod httpMethod, String path, Class<T> responseType, Object body) {
        RestTemplate restTemplate = getClient();

        try {
            return restTemplate
                    .exchange(path, httpMethod, getHttpEntity(body), responseType);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            Object responseBody = null;
            try {
                responseBody = new ObjectMapper().readValue(exception.getResponseBodyAsString(), responseType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(responseBody, exception.getStatusCode());
        }
    }

    protected <T> ResponseEntity<T> executeRequest(HttpMethod httpMethod, String path, Class<T> responseType) {
        RestTemplate restTemplate = getClient();

        try {
            return restTemplate
                    .exchange(path, httpMethod, getHttpEntity(null), responseType);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            Object responseBody = null;
            try {
                responseBody = new ObjectMapper().readValue(exception.getResponseBodyAsString(), responseType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error during parsing response!", e);
            }
            return new ResponseEntity(responseBody, exception.getStatusCode());
        }
    }

    private RestTemplate getClient() {
        return new RestTemplateBuilder()
                .rootUri(getBaseUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    private HttpEntity<?> getHttpEntity(Object body) {
        return new HttpEntity<>(objectToJsonString(body));
    }

    private String objectToJsonString(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldnt serialize object to json!", e);
        }
    }
}
