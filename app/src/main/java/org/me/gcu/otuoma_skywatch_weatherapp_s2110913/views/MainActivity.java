package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewSkipToMainPage = findViewById(R.id.textViewSkipToMainPage);
        textViewSkipToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherLatestObservationsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onGetStartedClicked(View view) {
        Intent intent = new Intent(this, ManualActivity.class);
        startActivity(intent);
    }
}
