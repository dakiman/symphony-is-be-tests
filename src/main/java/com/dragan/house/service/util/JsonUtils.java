package com.dragan.house.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private JsonUtils() {

    }

    public static String objectToJsonString(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldnt serialize object to json!", e);
        }
    }
}