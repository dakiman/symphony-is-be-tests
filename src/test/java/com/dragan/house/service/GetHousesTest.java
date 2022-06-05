package com.dragan.house.service;

import com.dragan.house.service.http.HousesClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class GetHousesTest {

    @Autowired
    HousesClient housesClient;

    @Test
    void getHouses() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(housesClient.getHouses("1", "1000", "austin").getBody()));
    }
}
