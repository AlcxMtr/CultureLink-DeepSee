package com.example.deepsee.weather;

public interface WeatherListener {
    void onWeatherReceived(int temperature, int rainChance, int humidity, int windSpeed, String condition);
    void onWeatherError(String errorMessage);
}
