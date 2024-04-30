package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;


import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils.NetworkUtil;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils.NightModeUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model.ThreeDayData;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers.ThreeDayForecastApi;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers.WeatherApi;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model.WeatherData;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers.ThreeDayForecastXmlPullParserHandler;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers.WeatherXmlPullParserHandler;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers.LocationIdManager;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherLatestObservationsActivity extends Activity {

    private Spinner spinner;
    private TextView textWeatherType;
    private TextView textDate;
    private TextView textTime;
    private TextView textTemperature;
    private TextView textWindSpeed;
    private TextView textHumidity;
    private TextView textPressure;
    private TextView textVisibility;

    private TextView textTodayLabel;
    private TextView textTodayWeather;
    private TextView textTemperatureMax;
    private TextView textTemperatureMin;


    private TextView textTomorrowLabel;
    private TextView textTomorrowWeather;
    private TextView textTomorrowTemperatureMax;
    private TextView textTomorrowTemperatureMin;


    private TextView textLaterLabel;
    private TextView textLaterWeather;
    private TextView textLaterTemperatureMax;
    private TextView textLaterTemperatureMin;


    private ExecutorService executorService;

    String latitude;
    String longitude;

    List<ThreeDayData> forecasts;

    private LocationIdManager locationIdManager;
    private String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme dynamically


        super.onCreate(savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            NightModeUtil.applyTheme(this);
            setContentView(R.layout.activity_weather_latest_landscape_observations);
        } else {
            // Portrait mode
            NightModeUtil.applyTheme(this);
            setContentView(R.layout.activity_weather_latest_observations);
        }

        // Check if dark mode is enabled and update UI accordingly
        // boolean isDarkModeEnabled = NightModeUtil.isDarkModeEnabled(this);

        initializeUI();
    }

    private void fetchWeatherDataForLocation(int locationId) {
        executorService.execute(() -> {
            WeatherApi weatherApi = new WeatherApi();
            try {
                InputStream in = weatherApi.fetchWeatherDataForLocation(String.valueOf(locationId));
                if (in == null) {
                    throw new IOException("Failed to fetch weather data.");
                }
                List<WeatherData> weatherDataList = new WeatherXmlPullParserHandler().parse(in);
                // Assuming you want to update the UI with the first WeatherData object in the list
                if (!weatherDataList.isEmpty()) {
                    WeatherData weatherData = weatherDataList.get(0); // Get the first WeatherData object
                    runOnUiThread(() -> updateUI(weatherData)); // Update UI on the main thread
                } else {
                    runOnUiThread(() -> {
                        textTemperature.setText("No weather data available.");
                        textWindSpeed.setText("No weather data available.");
                        textHumidity.setText("No weather data available.");
                        textPressure.setText("No weather data available.");
                        textVisibility.setText("No weather data available.");
                    });
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    textTemperature.setText("Error fetching weather data.");
                    textWindSpeed.setText("Error fetching weather data.");
                    textHumidity.setText("Error fetching weather data.");
                    textPressure.setText("Error fetching weather data.");
                    textVisibility.setText("Error fetching weather data.");
                });
            }
        });
    }

    private void fetchThreeDayWeatherDataForLocation(int locationId) {
        executorService.execute(() -> {
            ThreeDayForecastApi threeDayForecastApi = new ThreeDayForecastApi();
            try {
                InputStream in = threeDayForecastApi.fetchThreeDayWeatherDataForLocation(String.valueOf(locationId));
                if (in == null) {
                    throw new IOException("Failed to fetch weather data.");
                }
                ThreeDayForecastXmlPullParserHandler parserHandler = new ThreeDayForecastXmlPullParserHandler();
                forecasts = parserHandler.parse(in);
                Log.d("UpdateUIS", "Received ThreeDayData: " + forecasts);
                if (!forecasts.isEmpty()) {
                    // Assuming you have a method to update the UI with ThreeDayData
                    runOnUiThread(() ->
                    {
                        updateUI(forecasts.get(0));
                    });

                    // Extract latitude and longitude from the last item in the forecasts list
                    String latitude = forecasts.get(forecasts.size() - 1).getLatitude();
                    String longitude = forecasts.get(forecasts.size() - 1).getLongitude();
                    String locationName = forecasts.get(forecasts.size() - 1).getLocationName();

                    // Pass latitude and longitude to BottomNavigationHandler
                    BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this, latitude, longitude, locationName   );
                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                    bottomNavigationView.setOnItemSelectedListener(bottomNavigationHandler::navigateToActivity);
                } else {
                    runOnUiThread(() -> textTodayLabel.setText("No weather data available."));
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                runOnUiThread(() -> textTodayLabel.setText("Error fetching weather data: " + e.getMessage()));
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    private void updateUI(Object data) {
        if (data instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) data;

            // Correctly set the weather type using the placeholder
//            String weatherType = getString(R.string.weather_type_placeholder, weatherData.getWeatherType());
//            textWeatherType.setText(weatherType);

            // Correctly set the date using the placeholder
            String date = getString(R.string.date_placeholder, weatherData.getDate());
            textDate.setText(date);

            // Correctly set the time using the placeholder
            String time = getString(R.string.time_placeholder, weatherData.getTime());
            textTime.setText(time);

            // Correctly set the temperature using the placeholder
            String temperature = getString(R.string.temperature_placeholder, weatherData.getTemperature());
            textTemperature.setText(temperature);

            // Set the wind speed, humidity, pressure, and visibility without placeholders
            textWindSpeed.setText(weatherData.getWindSpeed());
            textHumidity.setText(weatherData.getHumidity());
            textPressure.setText(weatherData.getPressure());
            textVisibility.setText(weatherData.getVisibility());
        } else if (data instanceof ThreeDayData) {
            // Handle ThreeDayData

            ThreeDayData threeDayData = (ThreeDayData) data;

            Log.d("UpdateUI", "Received ThreeDayData: " + threeDayData);
            // Update today's forecast
            String todayLabel = getString(R.string.today, threeDayData.getTodayLabel());

            textTodayLabel.setText(todayLabel);
            String todayWeather = getString(R.string.today_Weather, threeDayData.getTodayWeather());

            textTodayWeather.setText(todayWeather);

            todayWeather = threeDayData.getTodayWeather();
            textWeatherType.setText(todayWeather);

            String todayMaxTemp = getString(R.string.maxTemp_placeholder, threeDayData.getTemperatureMax());

            textTemperatureMax.setText(todayMaxTemp);
            String todayMinTemp = getString(R.string.minTemp_placeholder, threeDayData.getTemperatureMin());

            textTemperatureMin.setText(todayMinTemp);

            // Update tomorrow's forecast
            String tomorrowLabel = getString(R.string.tomorrow, threeDayData.getTomorrowLabel());

            textTomorrowLabel.setText(tomorrowLabel);
            String tomorrowWeather = getString(R.string.tomorrow_Weather, threeDayData.getTomorrowWeather());

            textTomorrowWeather.setText(tomorrowWeather);
            String tomorrowMaxTemp = getString(R.string.tomorrow_maxTemp_placeholder, threeDayData.getTomorrowTemperatureMax());

            textTomorrowTemperatureMax.setText(tomorrowMaxTemp);
            String tomorrowMinTemp = getString(R.string.tomorrow_minTemp_placeholder, threeDayData.getTomorrowTemperatureMin());

            textTomorrowTemperatureMin.setText(tomorrowMinTemp);

            // Update the day after tomorrow's forecast
            String laterLabel = getString(R.string.later, threeDayData.getLaterLabel());

            textLaterLabel.setText(laterLabel);
            String laterWeather = getString(R.string.later_Weather, threeDayData.getLaterWeather());

            textLaterWeather.setText(laterWeather);
            String laterMaxTemp = getString(R.string.later_maxTemp_placeholder, threeDayData.getLaterTemperatureMax());

            textLaterTemperatureMax.setText(laterMaxTemp);
            String laterMinTemp = getString(R.string.later_minTemp_placeholder, threeDayData.getLaterTemperatureMin());

            textLaterTemperatureMin.setText(laterMinTemp);

            latitude = threeDayData.getLatitude();
            longitude = threeDayData.getLongitude();
            Log.d("FIOP",latitude);
            Log.d("FIOP",longitude);



        } else {
            // Handle other types of data or invalid data
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            Log.d("Orientation", "Landscape");
            setContentView(R.layout.activity_weather_latest_landscape_observations);
            initializeUI();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // Portrait mode
            Log.d("Orientation", "Portrait");
            setContentView(R.layout.activity_weather_latest_observations);
            initializeUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void initializeUI() {

        spinner = findViewById(R.id.spinnerLocation);
        textWeatherType = findViewById(R.id.textWeatherType);
        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        textTemperature = findViewById(R.id.textTemperature);
        textWindSpeed = findViewById(R.id.textWindSpeed);
        textHumidity = findViewById(R.id.textHumidity);
        textPressure = findViewById(R.id.textPressure);
        textVisibility = findViewById(R.id.textVisibility);

        textTodayLabel = findViewById(R.id.todayLabel);
        textTodayWeather = findViewById(R.id.textTodayWeather);
        textTemperatureMax = findViewById(R.id.textTemperatureMax);
        textTemperatureMin = findViewById(R.id.textTemperatureMin);

        textTomorrowLabel = findViewById(R.id.tomorrowLabel);
        textTomorrowWeather = findViewById(R.id.textTomorrowWeather);
        textTomorrowTemperatureMax = findViewById(R.id.textTomorrowTemperatureMax);
        textTomorrowTemperatureMin = findViewById(R.id.textTomorrowTemperatureMin);

        textLaterLabel = findViewById(R.id.laterLabel);
        textLaterWeather = findViewById(R.id.textLaterWeather);
        textLaterTemperatureMax = findViewById(R.id.textLaterTemperatureMax);
        textLaterTemperatureMin = findViewById(R.id.textLaterTemperatureMin);

        locationIdManager = new LocationIdManager();

        List<String> locations = Arrays.asList(getResources().getStringArray(R.array.locations));
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, locations);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String location = (String) parent.getItemAtPosition(position);
                locationName = location;
                int locationId = locationIdManager.getLocationId(location);
                if (!NetworkUtil.isNetworkAvailable(WeatherLatestObservationsActivity.this)) {
                    // Display a message to the user or take any other appropriate action
                    runOnUiThread(() -> {
                        textTemperature.setText("No internet connection.");
                        textWindSpeed.setText("No internet connection.");
                        textHumidity.setText("No internet connection.");
                        textPressure.setText("No internet connection.");
                        textVisibility.setText("No internet connection.");

                        new AlertDialog.Builder(WeatherLatestObservationsActivity.this)
                                .setTitle("No Internet Connection")
                                .setMessage("Please enable Wi-Fi or your data package for a better experience.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // You can add any action to be performed when the user clicks OK
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    });
                } else {
                    fetchWeatherDataForLocation(locationId);
                    fetchThreeDayWeatherDataForLocation(locationId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Log.d("LocationName: ", locationName);
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this, latitude, longitude, locationName);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationHandler::navigateToActivity);

        executorService = Executors.newSingleThreadExecutor();
    }


}
