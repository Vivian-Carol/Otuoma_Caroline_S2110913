package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers;

import android.util.Log;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model.ThreeDayData;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreeDayForecastXmlPullParserHandler {
    private static final String ns = null;

    public List<ThreeDayData> parse(InputStream is) throws XmlPullParserException, IOException {
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

    private List<ThreeDayData> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<ThreeDayData> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private List<ThreeDayData> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<ThreeDayData> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        ThreeDayData item = new ThreeDayData();
        int itemIndex = 0;
        String locationName = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                String title = readText(parser);
                // Extract the location name from the title
                // Assuming the format is "BBC Weather - Forecast for [Location Name]"
                String[] titleParts = title.split("Forecast for");

                if (titleParts.length > 1) {
                    locationName = titleParts[1].trim(); // Trim any leading/trailing spaces
                    // Ensure to set the locationName on the current item
                    item.setLocationName(locationName);
                }
            } else if (name.equals("item")) {
                Log.d("Logging", String.valueOf(itemIndex));
                item = readItem(parser, item, itemIndex);
                Log.d("Loggingx", String.valueOf(item));
                itemIndex++;
                if (itemIndex == 3) { // Assuming we only want the first 3 items
                    items.add(item);
                    item = new ThreeDayData(); // Reset for the next item
                    itemIndex = 0; // Reset index for the next item
                }
            } else {
                skip(parser);
            }
        }
        return items;
    }





    private ThreeDayData readItem(XmlPullParser parser, ThreeDayData item, int itemIndex) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String geoPoint = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("title")) {
                String title = readText(parser);
                String today = title.split(":")[0];

                // Adjusted pattern to make the maximum temperature part optional
                Pattern pattern = Pattern.compile("(\\w+\\s*:)(.*?), Minimum Temperature: (-?\\d+°C) \\(\\d+°F\\)(?: Maximum Temperature: (-?\\d+°C) \\(\\d+°F\\))?");
                Matcher matcher = pattern.matcher(title);
                String dayLabel = today;

                String weather = null;
                String minTemp = null;
                String maxTemp = null;

                if (matcher.find()) {
                    weather = matcher.group(2);
                    minTemp = matcher.group(3);
                    // Check if the maximum temperature group is present and not null
                    if (matcher.groupCount() >= 4 && matcher.group(4) != null) {
                        maxTemp = matcher.group(4);
                    }
                }

                switch (itemIndex) {
                    case 0: // Today
                        item.setTodayLabel(dayLabel);
                        item.setTodayWeather(weather);
                        item.setTemperatureMax(maxTemp);
                        item.setTemperatureMin(minTemp);
                        break;
                    case 1: // Tomorrow
                        item.setTomorrowLabel(dayLabel);
                        item.setTomorrowWeather(weather);
                        item.setTomorrowTemperatureMax(maxTemp);
                        item.setTomorrowTemperatureMin(minTemp);
                        break;
                    case 2: // Later
                        item.setLaterLabel(dayLabel);
                        item.setLaterWeather(weather);
                        item.setLaterTemperatureMax(maxTemp);
                        item.setLaterTemperatureMin(minTemp);
                        break;
                }
            }
            else if (name.equals("point")) {
                geoPoint = readGeoPoint(parser);
                String[] latLong = geoPoint.split(" ");

                if (latLong.length == 2) {
                    item.setLatitude(latLong[0]);
                    item.setLongitude(latLong[1]);
                }
            } else {
                skip(parser);
            }
        }

        Log.d("DFH",item.toString());
        return item;
    }





    private String readGeoPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "point");
        String geoPoint = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "point");
        return geoPoint;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, parser.getName());
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, parser.getName());
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
}
