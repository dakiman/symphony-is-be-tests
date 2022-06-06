package com.dragan.house.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.dragan.house.service.util.JsonUtils.objectToJsonString;

public abstract class BaseClient {

    protected abstract String getBaseUrl();

    private final RestTemplate restTemplate;

    BaseClient() {
        restTemplate = initRestTemplate();
    }

    protected <T> ResponseEntity<T> executeRequest(HttpMethod httpMethod, String path, ParameterizedTypeReference typeReference, Object body, Map<String, ?> params) {
        if (!params.isEmpty()) {
            path = bindQueryParams(path, params);
        }

        return restTemplate.exchange(path, httpMethod, getHttpEntity(body), typeReference, params);
    }

    private String bindQueryParams(String path, Map<String, ?> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(getBaseUrl() + path);
        params.forEach((key, value) -> {
            if (value != null)
                uriBuilder.queryParam(key, value);
        });

        path = uriBuilder.build(false).toUriString();
        return path;
    }

    private RestTemplate initRestTemplate() {
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
