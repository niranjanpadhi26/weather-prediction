FROM amazoncorretto:17.0.7-alpine
EXPOSE 8080
ADD target/weather-prediction.jar weather-prediction.jar
ENTRYPOINT ["java", "-jar", "weather-prediction.jar"]