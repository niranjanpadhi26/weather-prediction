package com.sapient.weatherprediction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    private String name;
    private Coord coord;
}
