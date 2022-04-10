package com.daki.apitesting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Item {
    private int id;
    private String category_id;
    private String title_en;
    private String title_mk;
    private String description_en;
    @JsonProperty("description_mk")
    private String descriptionMk;
    private int active;
    private String pictures;
    private String created_at;
    private String updated_at;
}
