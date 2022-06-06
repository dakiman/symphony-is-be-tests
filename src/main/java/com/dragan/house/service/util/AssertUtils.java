package com.dragan.house.service.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AssertUtils {
    private AssertUtils() {
    }

    public static void assertResponseIsOkWithBody(ResponseEntity<?> response) {
        if(response.getStatusCode() != HttpStatus.OK)
            throw new AssertionError("Response status is not as 200 OK, found " + response.getStatusCode());

        if(response.getBody() == null)
            throw new AssertionError("Response body is null");
    }
}
