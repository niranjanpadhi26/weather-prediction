package com.sapient.weatherprediction.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherList {
    private long dt;
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private String dt_txt;
}
