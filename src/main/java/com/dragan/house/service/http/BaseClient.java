package com.dragan.house.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.dragan.house.service.util.JsonUtils.objectToJsonString;

public abstract class BaseClient {

    protected abstract String getBaseUrl();

    protected <T> ResponseEntity<T> executeRequest(HttpMethod httpMethod, String path, ParameterizedTypeReference typeReference, Object body, Map<String, ?> params) {
        RestTemplate restTemplate = getClient();

        try {
            return restTemplate
                    .exchange(path, httpMethod, getHttpEntity(body), typeReference, params);
        } catch (HttpServerErrorException | HttpClientErrorException exception) {
            Object responseBody = null;
            try {
                responseBody = new ObjectMapper().readValue(exception.getResponseBodyAsString(), typeReference.getClass());
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
                .build();
    }

    private HttpEntity<?> getHttpEntity(Object body) {
        return new HttpEntity<>(objectToJsonString(body));
    }

}
