package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model.WeatherData;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import android.os.Handler;
import android.util.Log;

import java.util.TimerTask;


public class WeatherXmlPullParserHandler {
    private static final String ns = null;

    private Timer timer;
    private Handler handler;
    private Runnable refreshTask;
    private long refreshIntervalMillis = 1000 * 60 * 60 * 4; // Refresh every 4 hours

    public WeatherXmlPullParserHandler() {
        refreshTask = () -> {
            // Perform weather data refresh here
            Log.d("WeatherXmlPullParserHandler", "Refreshing weather data...");
        };
        scheduleRefresh();
    }


    public List<WeatherData> parse(InputStream is) throws XmlPullParserException, IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            is.close();
        }
    }

    private List<WeatherData> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<WeatherData> weatherDataList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                weatherDataList.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return weatherDataList;
    }

    private List<WeatherData> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<WeatherData> weatherDataList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                weatherDataList.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return weatherDataList;
    }

    private WeatherData readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        WeatherData weatherData = new WeatherData();
        parser.require(XmlPullParser.START_TAG, ns, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                String title = readText(parser);
                weatherData.setWeatherType(extractWeatherType(title));
            } else if (name.equals("pubDate")) {
                String pubDate = readText(parser);
                weatherData.setDate(extractDate(pubDate));
                weatherData.setTime(extractTime(pubDate));
            } else if (name.equals("description")) {
                String description = readText(parser);
                extractWeatherDetails(description, weatherData);
            } else {
                skip(parser);
            }
        }
        return weatherData;
    }

    private String extractWeatherType(String title) {
        // Find the index of the comma which separates the weather type from the temperature
        int commaIndex = title.indexOf(",");

        // Check if comma is found
        if (commaIndex != -1) {
            // Extract the weather type from the beginning of the string up to the comma
            String weatherType = title.substring(title.lastIndexOf(":") + 1, commaIndex).trim();
            return weatherType;
        }

        return ""; // Return an empty string if the weather type cannot be extracted
    }

    private String extractDate(String pubDate) {
        // Assuming the date format is "Day, DD Mon YYYY"
        String[] parts = pubDate.split(" ");
        if (parts.length >= 4) {
            return parts[0] + " " + parts[1] + " " + parts[2] + ", " + parts[3];
        }
        return "";
    }

    private String extractTime(String pubDate) {
        // Remove commas to make parsing easier
        pubDate = pubDate.replace(",", "");
        // Split the string by spaces
        String[] parts = pubDate.split(" ");
        if (parts.length >= 5) {
            String time = parts[4];
            String timezone = parts[5];

            // Extract the hour part of the time
            String hourPart = time.substring(0, time.indexOf(":"));
            int hour = Integer.parseInt(hourPart);

            // Determine if the time is AM or PM
            String amPm = hour >= 12 ? "pm" : "am";

            if (hour == 0) {
                hour = 12;
                amPm = "am";
            } else if (hour == 12) {
                amPm = "pm";
            }

            time = String.format("%02d:%s", hour, time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
            time += " " + amPm;

            return time + " (" + timezone + ")";
        }
        return "";
    }



    private void extractWeatherDetails(String description, WeatherData weatherData) {
        // Assuming the description format remains consistent. Adjust parsing logic if the format changes.
        String[] parts = description.split(", ");
        for (String part : parts) {
            if (part.contains("Temperature:")) {
                String[] temperatureParts = part.trim().split(":")[1].trim().split(" ");
                String temperatureValue = temperatureParts[0].replace("Â°", "");
                weatherData.setTemperature(temperatureValue);
            } else if (part.contains("Wind Speed:")) {
                String[] windSpeedParts = part.trim().split(":")[1].trim().split(" ");
                weatherData.setWindSpeed(windSpeedParts[0]); // Extract only the wind speed value
            } else if (part.contains("Humidity:")) {
                String[] humidityParts = part.trim().split(":")[1].trim().split("%");
                weatherData.setHumidity(humidityParts[0]); // Extract only the humidity value
            } else if (part.contains("Pressure:")) {
                String[] pressureParts = part.trim().split(":")[1].trim().split(" ");
                weatherData.setPressure(pressureParts[0]); // Extract only the pressure value
            } else if (part.contains("Visibility:")) {
                String[] visibilityParts = part.trim().split(":")[1].trim().split(" ");
                weatherData.setVisibility(visibilityParts[0]); // Extract only the visibility value
            }
        }
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private void scheduleRefresh() {
        timer = new Timer();
        Calendar now = Calendar.getInstance();
        long currentTimeMillis = now.getTimeInMillis();
        long nextRefreshTimeMillis = calculateNextRefreshTimeMillis(currentTimeMillis);
        long delayMillis = nextRefreshTimeMillis - currentTimeMillis;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(refreshTask);
                scheduleRefresh();
            }
        }, delayMillis);
    }

    private long calculateNextRefreshTimeMillis(long currentTimeMillis) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        // Calculate the time of the next refresh (default times are 08:00 and 20:00)
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int nextRefreshHour = currentHour >= 20 ? 8 : 20;
        // If the current time is after the next refresh hour, set it to the next day
        if (currentHour >= 20) {
            now.add(Calendar.DAY_OF_YEAR, 1);
        }
        now.set(Calendar.HOUR_OF_DAY, nextRefreshHour);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }
}
