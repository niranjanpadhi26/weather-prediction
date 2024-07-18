package com.sapient.weatherprediction.service;



import com.sapient.weatherprediction.exceptions.WeatherServiceException;
import com.sapient.weatherprediction.model.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @CircuitBreaker(name = "weatherService", fallbackMethod = "getWeatherAlertsFallback")
//    @Retry(name = "weatherService")
    public WeatherResponse getWeather(String city) {
        String apiKey = "d2929e9483efc82c82c32ee7e02d563e";
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric";
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

    public String analyzeWeather(String city) {
        WeatherResponse weatherResponse = getWeather(city);
        StringBuilder alerts = new StringBuilder();
        List<String> alertList = new ArrayList<>();
        List<WeatherList> weatherList = weatherResponse.getList();

        long currentTimestamp = System.currentTimeMillis() / 1000;
        long nextDayTimestamp = currentTimestamp + 86400; // 86400 seconds in a day

        List<WeatherList> filteredList = weatherList.stream()
                .filter(data -> data.getDt() < nextDayTimestamp)
                .toList();

        AtomicBoolean isWeatherSet = new AtomicBoolean(false);
        AtomicBoolean windy = new AtomicBoolean(false);
        AtomicBoolean highTemp = new AtomicBoolean(false);
        filteredList.forEach(weatherList1 -> {

            for (Weather weather : weatherList1.getWeather()) {
                if (!isWeatherSet.get() && "Thunderstorm".equals(weather.getMain())) {
                    alerts.append("Don’t step out! A Storm is brewing! ");
                    isWeatherSet.set(true);
                    break;
                } else if (!isWeatherSet.get() && "Rain".equals(weather.getMain())) {
                    alerts.append("Rain is Expected . Carry umbrella! ");
                    isWeatherSet.set(true);
                    break;
                }
            }
            if (!windy.get() && weatherList1.getWind().getSpeed() > 10) {
                alerts.append("\\n");
                alerts.append("It’s too windy, watch out! ");
                windy.set(true);
            }
            if(!highTemp.get() && weatherList1.getMain().getTemp() > 40){
                alerts.append("\\n");
                alerts.append("It’s too hot, Apply Sunscreen! ");
                highTemp.set(true);
            }
        });

        return alerts.toString();
    }



    public UiViewModel getUiViewModel(String city) {
        WeatherResponse weatherResponse = getWeather(city);
        List<String> alertList = new ArrayList<>();
        List<WeatherList> weatherList = weatherResponse.getList();

//        long currentTimestamp = System.currentTimeMillis() / 1000;
//        long nextDayTimestamp = currentTimestamp + (86400 *3); // 86400 seconds in a day

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(2);

        Map<LocalDate, List<WeatherList>> groupedByDay = weatherList.stream()
                .filter(data -> {
                    LocalDate date = Instant.ofEpochSecond(data.getDt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    return !date.isBefore(currentDate) && !date.isAfter(endDate);
                })
                .collect(Collectors.groupingBy(data ->
                        Instant.ofEpochSecond(data.getDt())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                ));


        AtomicInteger itr= new AtomicInteger();
        UiViewModel uiViewModel = UiViewModel.builder()
                .predictionList(new ArrayList<>())
                .build();

        for (Map.Entry<LocalDate, List<WeatherList>> entry : groupedByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<WeatherList> value = entry.getValue();
            AtomicReference<Double> maxTemp = new AtomicReference<>(0.0);
            itr.set(0);
            List<WeatherList> weatherList1 = groupedByDay.get(date);
            maxTemp.set(Math.max(weatherList1.get(itr.get()).getMain().getTemp(), maxTemp.get()));
            Prediction prediction = Prediction.builder()
                    .date(date.toString())
                    .temp(String.valueOf(weatherList1.get(itr.getAndIncrement()).getMain().getTemp()))
                    .alertList(new ArrayList<>())
                    .build();
            List<AlertList> lis = prediction.getAlertList();
            for (WeatherList weather : weatherList1) {
                StringBuilder alerts = new StringBuilder();
                AlertList tempAlert = AlertList.
                        builder().
                        highTemp(String.valueOf(weather.getMain().getTemp_max())).
                        lowTemp(String.valueOf(weather.getMain().getTemp_min())).
                        temp(String.valueOf(weather.getMain().getTemp())).
                        time(String.valueOf(Instant.ofEpochSecond(weather.getDt()).atZone(ZoneId.systemDefault()).toLocalTime())).
                        description(weather.getWeather().get(0).getDescription()).
                        //alertMsg(weather.getWeather().get(0).getMain()).
                                icon(weather.getWeather().get(0).getIcon()).
                        build();

                if ("Thunderstorm".equals(weather.getWeather().get(0).getMain())) {
                    alerts.append("Don’t step out! A Storm is brewing! ");
                } else if ("Rain".equals(weather.getWeather().get(0).getMain())) {
                    alerts.append("Rain is Expected . Carry umbrella! ");
                }

                if (weather.getWind().getSpeed() > 10) {
                    if (!alerts.isEmpty())
                        alerts.append("and ");
                    alerts.append("It’s too windy, watch out! ");
                }
                if (weather.getMain().getTemp() > 40) {
                    if (!alerts.isEmpty())
                        alerts.append("and ");
                    alerts.append("It’s too hot, Apply Sunscreen! ");
                }
                tempAlert.setAlertMsg(alerts.toString());
                lis.add(tempAlert);
            }
            uiViewModel.getPredictionList().add(prediction);

        }

        return uiViewModel;
    }

    public String getWeatherAlertsFallback(String city, Throwable t) {

        throw new WeatherServiceException("Weather information is currently unavailable. Please try again later.");
    }
}
