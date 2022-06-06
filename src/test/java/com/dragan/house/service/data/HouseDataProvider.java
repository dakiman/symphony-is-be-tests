package com.dragan.house.service.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class HouseDataProvider {

    public static List<Integer> getMaxPrices() {
        return List.of(
                250000,
                500000,
                750000,
                1000000
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
                250000,
                500000,
                750000
        );
    }


}
