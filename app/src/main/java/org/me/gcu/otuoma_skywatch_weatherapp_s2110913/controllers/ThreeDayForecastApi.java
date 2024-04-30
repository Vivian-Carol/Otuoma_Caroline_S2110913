package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ThreeDayForecastApi {
    public InputStream fetchThreeDayWeatherDataForLocation(String locationId) throws IOException {
        HttpURLConnection urlConnection = null;

        try {
            System.out.println("Fetching weather data for location ID: " + locationId);
            URL url = new URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            return urlConnection.getInputStream();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
