package com.sapient.weatherprediction.exceptions;

public class WeatherServiceException extends RuntimeException{

    public WeatherServiceException(String message) {
        super(message);
    }
}
