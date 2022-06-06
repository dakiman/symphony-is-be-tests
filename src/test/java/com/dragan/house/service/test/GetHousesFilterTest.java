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
import static com.dragan.house.service.util.AssertUtils.assertResponseIsOkWithBody;
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
        List<House> houses = getHouses(null, null, null);

        assertThat(houses.size(), greaterThan(0));
    }

    @Test
    void getHousesMaxAndMinPriceEqual() {
        List<House> houses = getHouses(EXISTING_HOUSE_PRICE.toString(), EXISTING_HOUSE_PRICE.toString(), null);

        assertThat(houses.size(), greaterThan(0));
        houses.forEach(house -> assertThat(house.getPrice(), equalTo(EXISTING_HOUSE_PRICE)));
    }

    @ParameterizedTest(name = "Filter by invalid city {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "Au%20stin", "New%20%20$#York"})
    void getHousesInvalidCityFilter(String city) {
        List<House> houses = getHouses(null, null, city);

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by invalid min price = {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "123,000$", "123%20000"})
    void getHousesInvalidMinPriceFilter(String minPrice) {
        List<House> houses = getHouses(minPrice, null, null);

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by invalid max price = {0}")
    @ValueSource(strings = {"asdasasd", "123asd", "123,000$", "123%20000"})
    void getHousesInvalidMaxPriceFilter(String maxPrice) {
        List<House> houses = getHouses(null, maxPrice, null);

        assertTrue(houses.isEmpty());
    }

    @ParameterizedTest(name = "Filter by min price = {0}")
    @MethodSource("providerForMinPrices")
    void getHousesFilterByMinPrice(Integer minPrice) {
        List<House> houses = getHouses(minPrice.toString(), null, null);

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(minPrice));
        });
    }

    @ParameterizedTest(name = "Filter by max price = {0}")
    @MethodSource("providerForMaxPrices")
    void getHousesFilterByMaxPrice(Integer maxPrice) {
        List<House> houses = getHouses(null, maxPrice.toString(), null);

        houses.forEach(house -> {
            assertThat(house.getPrice(), lessThan(maxPrice));
        });
    }

    @ParameterizedTest(name = "Filter by city {0}")
    @MethodSource("providerForCities")
    void getHousesFilterByCity(String city) {
        List<House> houses = getHouses(null, null, city);
        assertThat(houses.size(), greaterThan(0));

        houses.forEach(house -> {
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by max price = {0} and min price = {1}")
    @MethodSource("providerForMaxAndMinPrices")
    void getHouseFilterByMinAndMaxPrice(Integer maxPrice, Integer minPrice) {
        List<House> houses = getHouses(minPrice.toString(), maxPrice.toString(), null);

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(minPrice)).and(lessThan(maxPrice))));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0} and min price = {1}")
    @MethodSource("providerForMinPricesAndCities")
    void getHousesFilterByMinPriceAndCity(String city, Integer minPrice) {
        List<House> houses = getHouses(minPrice.toString(), null, city);

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(100000));
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0} and max price = {1}")
    @MethodSource("providerForMaxPricesAndCities")
    void getHousesFilterByMaxPriceAndCity(String city, Integer maxPrice) {
        List<House> houses = getHouses(maxPrice.toString(), null, city);

        houses.forEach(house -> {
            assertThat(house.getPrice(), greaterThan(maxPrice));
            assertThat(house.getCity(), is(city));
        });
    }

    @ParameterizedTest(name = "Filter by city = {0}, max price = {1} and min price = {2}")
    @MethodSource("providerForMinPricesMaxPricesAndCities")
    void getHousesFilterByMinPriceMaxPriceAndCity(String city, Integer maxPrice, Integer minPrice) {
        List<House> houses = getHouses(minPrice.toString(), maxPrice.toString(), city);

        houses.forEach(house -> {
            assertThat(house.getPrice(), is(both(greaterThan(minPrice)).and(lessThan(maxPrice))));
            assertThat(house.getCity(), is(city));
        });
    }

    private List<House> getHouses(String minPrice, String maxPrice, String city) {
        ResponseEntity<List<House>> response = housesClient.getHouses(minPrice, maxPrice, city);
        assertResponseIsOkWithBody(response);

        return response.getBody();
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
                if (maxPrice > minPrice) arguments.add(Arguments.of(maxPrice, minPrice));
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
