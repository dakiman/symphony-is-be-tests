package com.dragan.house.service;

import com.dragan.house.service.http.HousesClient;
import com.dragan.house.service.model.House;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
class GetHousesFilterTest {

    @Autowired
    HousesClient housesClient;

    @Test
    void getHouses() throws Exception {
    }

    @Test
    void getHousesFilterByMinPrice() {
        ResponseEntity<List<House>> response = housesClient.getHouses(250000, null, null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(250000));
        });
    }

    @Test
    void getHousesFilterByMaxPrice() {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, 250000, null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), lessThan(250000));
        });
    }

    @Test
    void getHousesFilterByCity() {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, null, "Austin");
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getCity(), is("Austin"));
        });
    }

    @Test
    void getHouseFilterByMinAndMaxPrice() {
        ResponseEntity<List<House>> response = housesClient.getHouses(100000, 250000, null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(100000)).and(lessThan(250000))));
        });
    }

    @Test
    void getHousesFilterByMinPriceAndCity() {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, 250000, "Austin");
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), lessThan(250000));
            assertThat(house.getCity(), is("Austin"));
        });
    }

    @Test
    void getHousesFilterByMaxPriceAndCity() {
        ResponseEntity<List<House>> response = housesClient.getHouses(100000, null, "Austin");
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(100000));
            assertThat(house.getCity(), is("Austin"));
        });
    }

    @Test
    void getHousesFilterByMinPriceMaxPriceAndCity() {
        ResponseEntity<List<House>> response = housesClient.getHouses(100000, 250000, "Austin");
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(100000)).and(lessThan(250000))));
            assertThat(house.getCity(), is("Austin"));
        });
    }


}
