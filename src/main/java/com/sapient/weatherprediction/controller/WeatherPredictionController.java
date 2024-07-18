package com.sapient.weatherprediction.controller;

import com.sapient.weatherprediction.model.UiViewModel;
import com.sapient.weatherprediction.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Weather", description = "Weather API")
public class WeatherPredictionController {

    private final WeatherService weatherService;

    @Autowired
    public  WeatherPredictionController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

//    @ApiOperation(value = "Get weather alerts", notes = "Fetch weather data and provide alerts for high winds and thunderstorms.")
    @GetMapping("/weather")
    public String getWeather( @RequestParam String city) {
//        WeatherResponse weatherResponse = weatherService.getWeather(city);
        return weatherService.analyzeWeather(city);
    }

    @GetMapping("/getPredictions")
    @Operation(summary = "Get Weather Alerts", description = "Fetches weather alerts for a specified city.")
    public UiViewModel getPredictions(@Parameter(description = "Name of the city to fetch weather alerts for", example = "London") @RequestParam String city) {
//        WeatherResponse weatherResponse = weatherService.getWeather(city);
        UiViewModel response = weatherService.getUiViewModel(city);
        Link selfLink = linkTo(methodOn(WeatherPredictionController.class).getPredictions(city)).withSelfRel();
        Link hydLink = linkTo(methodOn(WeatherPredictionController.class).getPredictions("Hyderabad")).withRel("Hyderabad");
        Link delhiLink = linkTo(methodOn(WeatherPredictionController.class).getPredictions("Delhi")).withRel("Delhi");
        Link mumbaiLink = linkTo(methodOn(WeatherPredictionController.class).getPredictions("Mumbai")).withRel("Mumbai");
        Link chennaiLink = linkTo(methodOn(WeatherPredictionController.class).getPredictions("Chennai")).withRel("Chennai");
        response.add(selfLink,hydLink,delhiLink,mumbaiLink,chennaiLink);
        return response;
    }
}

