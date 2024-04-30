package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApi {
    // Method to fetch weather data for a specified location from the RSS feed API
    public InputStream fetchWeatherDataForLocation(String locationId) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            // Construct the URL for the specified location's RSS feed API
            URL url = new URL("https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + locationId);

            // Open connection to the API URL
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Get the InputStream
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return inputStream;
    }
}

