package com.dragan.house.service.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class HouseDataProvider {
    public static Integer EXISTING_HOUSE_PRICE = 799000;

    public static List<Integer> getMaxPrices() {
        return List.of(
                500000,
                600000,
                700000,
                800000
        );
    }

    public static List<String> getCities() {
        return List.of(
                "Austin",
                "New York",
                "E. New York"
        );
    }

    public static List<Integer> getMinPrices() {
        return List.of(
                100000,
                200000,
                300000,
                400000
        );
    }

}
