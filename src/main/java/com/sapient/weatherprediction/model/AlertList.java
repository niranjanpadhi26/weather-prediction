package com.sapient.weatherprediction.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertList {
    private String highTemp;
    private String lowTemp;
    private String temp;
    private String time;
    private String description;
    private String alertMsg;
    private String icon;
}
