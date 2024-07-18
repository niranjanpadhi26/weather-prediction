package com.sapient.weatherprediction.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemperatureModel {

    private String day;
    private String low;
    private String max;
}
