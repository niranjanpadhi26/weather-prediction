package com.sapient.weatherprediction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private String cod;
    private int cnt;
    private List<WeatherList> list;
    private City city;
}
