package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views.MapDisplayActivity;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views.SettingsActivity;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views.WeatherLatestObservationsActivity;


public class BottomNavigationHandler {

    private Context context;
    private String latitude;
    private String longitude;
    private String locationName;

    // Existing constructor
    public BottomNavigationHandler(Context context, String latitude, String longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // New constructor to include locationName
    public BottomNavigationHandler(Context context, String latitude, String longitude, String locationName) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName; // Store the locationName
    }

    public boolean navigateToActivity(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_home) {
            // Redirect to WeatherLatestObservationsActivity
            Intent intent = new Intent(context, WeatherLatestObservationsActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.navigation_map) {
            // Check if latitude and longitude are not null or empty
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
                // Open MapDisplayActivity
                Intent intent = new Intent(context, MapDisplayActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("locationName", locationName);
                // Log.d("Map's Location Mofo", locationName);
                context.startActivity(intent);
                return true;
            } else {
                // Show a Toast message if latitude or longitude is missing
                Toast.makeText(context, "Please select a location first.", Toast.LENGTH_SHORT).show();

                // Redirect to WeatherLatestObservationsActivity after showing the Toast
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, WeatherLatestObservationsActivity.class);
                        context.startActivity(intent);
                    }
                }, 100); //delay

                return true;
            }
        } else if (id == R.id.navigation_settings) {
            // Redirect to SettingsActivity
            Intent settingsIntent = new Intent(context, SettingsActivity.class);
            context.startActivity(settingsIntent);
            return true;
        }
        return false;
    }
}
