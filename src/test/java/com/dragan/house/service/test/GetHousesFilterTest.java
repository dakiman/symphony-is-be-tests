package com.dragan.house.service.test;

import com.dragan.house.service.http.HousesClient;
import com.dragan.house.service.model.House;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.dragan.house.service.data.HouseDataProvider.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class GetHousesFilterTest {

    @Autowired
    HousesClient housesClient;

    @Test
    void getHousesNoFilter() {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, null, null);
        List<House> houses = response.getBody();

        assertThat(houses.size(), greaterThan(0));
    }

    @Test
    void getHousesMaxAndMinPriceEqual() {
        ResponseEntity<List<House>> response = housesClient.getHouses(EXISTING_HOUSE_PRICE.toString(), EXISTING_HOUSE_PRICE.toString(), null);
        List<House> houses = response.getBody();

        assertThat(houses.size(), greaterThan(0));
        assertThat(houses.get(0).getPrice(), equalTo(EXISTING_HOUSE_PRICE));
    }

    @ParameterizedTest(name = "Filter by invalid city {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "Au%20stin", "New%20%20$#York"})
    void getHousesInvalidCityFilter(String city) {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, null, city);
        List<House> houses = response.getBody();

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by invalid min price = {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "123,000$", "123%20000"})
    void getHousesInvalidMinPriceFilter(String minPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice, null, null);
        List<House> houses = response.getBody();

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by invalid max price = {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "123,000$", "123%20000"})
    void getHousesInvalidMaxPriceFilter(String maxPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, maxPrice, null);
        List<House> houses = response.getBody();

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by min price = {0}")
    @MethodSource("providerForMinPrices")
    void getHousesFilterByMinPrice(Integer minPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice.toString(), null, null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(minPrice));
        });
    }

    @ParameterizedTest(name = "Filter by max price = {0}")
    @MethodSource("providerForMaxPrices")
    void getHousesFilterByMaxPrice(Integer maxPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, maxPrice.toString(), null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), lessThan(maxPrice));
        });
    }

    @ParameterizedTest(name = "Filter by city {0}")
    @MethodSource("providerForCities")
    void getHousesFilterByCity(String city) {
        ResponseEntity<List<House>> response = housesClient.getHouses(null, null, city);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by max price = {0} and min price = {1}")
    @MethodSource("providerForMaxAndMinPrices")
    void getHouseFilterByMinAndMaxPrice(Integer maxPrice, Integer minPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice.toString(), maxPrice.toString(), null);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(minPrice)).and(lessThan(maxPrice))));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0} and min price = {1}")
    @MethodSource("providerForMinPricesAndCities")
    void getHousesFilterByMinPriceAndCity(String city, Integer minPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice.toString(), null, city);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(100000));
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0} and max price = {1}")
    @MethodSource("providerForMaxPricesAndCities")
    void getHousesFilterByMaxPriceAndCity(String city, Integer maxPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(maxPrice.toString(), null, city);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(maxPrice));
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0}, max price = {1} and min price = {2}")
    @MethodSource("providerForMinPricesMaxPricesAndCities")
    void getHousesFilterByMinPriceMaxPriceAndCity(String city, Integer maxPrice, Integer minPrice) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice.toString(), maxPrice.toString(), city);
        List<House> houses = response.getBody();

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(minPrice)).and(lessThan(maxPrice))));
            assertThat(house.getCity(), is(city));
        });
    }

    private static List<String> providerForCities() {
        return getCities();
    }

    private static List<Integer> providerForMaxPrices() {
        return getMaxPrices();
    }

    private static List<Integer> providerForMinPrices() {
        return getMinPrices();
    }

    private static List<Arguments> providerForMaxAndMinPrices() {
        List<Integer> maxPrices = getMaxPrices();
        List<Integer> minPrices = getMinPrices();
        List<Arguments> arguments = new ArrayList<>();

        maxPrices.forEach(maxPrice -> {
            minPrices.forEach(minPrice -> {
                if (maxPrice > minPrice)
                    arguments.add(Arguments.of(maxPrice, minPrice));
            });
        });

        return arguments;
    }

    private static List<Arguments> providerForMaxPricesAndCities() {
        List<String> cities = getCities();
        List<Integer> maxPrices = getMaxPrices();
        List<Arguments> arguments = new ArrayList<>();

        maxPrices.forEach(maxPrice -> {
            cities.forEach(city -> {
                arguments.add(Arguments.of(city, maxPrice));
            });
        });

        return arguments;
    }

    private static List<Arguments> providerForMinPricesAndCities() {
        List<String> cities = getCities();
        List<Integer> minPrices = getMinPrices();
        List<Arguments> arguments = new ArrayList<>();

        minPrices.forEach(minPrice -> {
            cities.forEach(city -> {
                arguments.add(Arguments.of(city, minPrice));
            });
        });

        return arguments;
    }

    private static List<Arguments> providerForMinPricesMaxPricesAndCities() {
        List<String> cities = getCities();
        List<Integer> maxPrices = getMaxPrices();
        List<Integer> minPrices = getMinPrices();
        List<Arguments> arguments = new ArrayList<>();

        maxPrices.forEach(maxPrice -> {
            minPrices.forEach(minPrice -> {
                if (maxPrice > minPrice) {
                    cities.forEach(city -> {
                        arguments.add(Arguments.of(city, maxPrice, minPrice));
                    });
                }
            });
        });

        return arguments;
    }

}
