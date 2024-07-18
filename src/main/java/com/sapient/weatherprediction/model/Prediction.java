package com.sapient.weatherprediction.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class Prediction {

    private String date;
    private String temp;
    private List<AlertList> alertList;
}
