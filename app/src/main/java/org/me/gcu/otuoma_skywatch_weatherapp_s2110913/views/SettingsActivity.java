package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;


import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils.NightModeUtil;



public class SettingsActivity extends AppCompatActivity {

    // Apply the theme dynamically
//      NightModeUtil.applyTheme(this);
    private boolean isUpdatingNightMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightModeUtil.applyTheme(this);
        setContentView(R.layout.activity_settings);
        setTheme(R.style.Base_Theme_Otuoma_SkyWatch_WeatherApp_S2110913);


        ToggleButton toggleButton = findViewById(R.id.toggleButton1);
        toggleButton.setChecked(NightModeUtil.isDarkModeEnabled(this)); // Set toggle state

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NightModeUtil.setNightMode(this, isChecked);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this, null, null);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationHandler::navigateToActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the UI to reflect the current night mode setting
        ToggleButton toggleButton = findViewById(R.id.toggleButton1);
        if (toggleButton != null && !isUpdatingNightMode) {
            boolean currentNightMode = NightModeUtil.isDarkModeEnabled(this);
            if (toggleButton.isChecked() != currentNightMode) {
                toggleButton.setChecked(currentNightMode);
            }
        }
    }
}
