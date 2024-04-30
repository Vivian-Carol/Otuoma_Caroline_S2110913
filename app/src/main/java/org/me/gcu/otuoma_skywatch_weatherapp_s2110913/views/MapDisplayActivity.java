package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils.NightModeUtil;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;

public class MapDisplayActivity extends AppCompatActivity implements OnMapReadyCallback {

    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme dynamically
        NightModeUtil.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        boolean isDarkModeEnabled = NightModeUtil.isDarkModeEnabled(this);


        // Retrieve the location name from the intent extras
        Bundle extras = getIntent().getExtras();
        String locationName = null;
        if (extras != null) {
            locationName = extras.getString("locationName");
            Log.d("YourActivityTag", "Retrieved locationName: " + locationName);
        } else {
            Log.d("YourActivityTag", "No extras found in intent");
        }

        // Correctly obtain the SupportMapFragment using its own ID
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // Use the ID of the SupportMapFragment
        mapFragment.getMapAsync(this);

        // Display the location name in the TextView
        TextView textView = findViewById(R.id.textView);
        if (locationName != null) {
            textView.setText(locationName);
        } else {
            textView.setText("Location not available");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this, latitude, longitude, locationName);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationHandler::navigateToActivity);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
        String latitudeString = extras.getString("latitude");
        String longitudeString = extras.getString("longitude");

        if (latitudeString != null && longitudeString != null) {
            try {
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                LatLng location = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Update UI to indicate failure
                TextView textView = findViewById(R.id.textView);
                textView.setText("Location could not be determined.");
            }
        } else {
            // Update UI to indicate failure
            TextView textView = findViewById(R.id.textView);
            textView.setText("Location could not be determined.");
        }
    } else {
        // Update UI to indicate failure
        TextView textView = findViewById(R.id.textView);
        textView.setText("Location could not be determined.");
    }
}

}

