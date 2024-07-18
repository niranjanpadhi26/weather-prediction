package com.sapient.weatherprediction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {
    private double speed;
    private int deg;
    private double gust;
}

