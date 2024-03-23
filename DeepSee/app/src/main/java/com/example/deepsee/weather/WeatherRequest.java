package com.example.deepsee.weather;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherRequest {

    private int temperature;
    private int rainChance;
    private int humidity;
    private int windSpeed;
    private String condition;

    public WeatherRequest(RequestQueue requestQueue,  WeatherListener listener, double latitude, double longitude) {
        getWeatherInfo(requestQueue, listener, latitude, longitude);
    }

    private void getWeatherInfo(RequestQueue requestQueue, WeatherListener listener, double latitude, double longitude) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=3ea1bd4ac87f447685323119242303&q=" + latitude + "," + longitude + "&days=1&aqi=no&alerts=no";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject current = response.getJSONObject("current");
                    temperature = current.getInt("temp_c");
                    rainChance = current.getInt("cloud");
                    humidity = current.getInt("humidity");
                    windSpeed = current.getInt("wind_kph");
                    condition = current.getJSONObject("condition").getString("text");
                    listener.onWeatherReceived(temperature, rainChance, humidity, windSpeed, condition);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WeatherRequest", "Error fetching weather data: " + error.getMessage());
                listener.onWeatherError(error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public int getTemperature () {
        return temperature;
    }

}
