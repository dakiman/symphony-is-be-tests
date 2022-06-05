
package com.dragan.house.service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class House {

    private String id;
    private Integer mlsId;
    private String mlsListingId;
    private String propertyType;
    private String formattedAddress;
    private String address;
    private String zip;
    private String city;
    private String state;
    private List<Float> location = null;
    private Integer bedrooms;
    private Integer bathrooms;
    private String listDate;
    private String mlsUpdateDate;
    private String priceDisplay;
    private Integer price;
    private Integer squareFeet;
    private Hero hero;

}
