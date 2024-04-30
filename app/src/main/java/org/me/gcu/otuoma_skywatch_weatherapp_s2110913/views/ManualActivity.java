package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;
import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils.NightModeUtil;

public class ManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NightModeUtil.applyTheme(this);
        setContentView(R.layout.manual_activity);
        setTheme(R.style.Base_Theme_Otuoma_SkyWatch_WeatherApp_S2110913);

        TextView textViewSkipToMainPage = findViewById(R.id.textViewSkipToMainPage);
        textViewSkipToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualActivity.this, WeatherLatestObservationsActivity.class);
                startActivity(intent);
            }
        });
    }
}
