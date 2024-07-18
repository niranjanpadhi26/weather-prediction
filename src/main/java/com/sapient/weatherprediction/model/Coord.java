package com.sapient.weatherprediction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coord {
    private double lat;
    private double lon;
}
