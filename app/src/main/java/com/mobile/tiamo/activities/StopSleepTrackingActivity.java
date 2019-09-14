package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobile.tiamo.R;
import com.mobile.tiamo.services.ScreenOnAndOffService;

public class StopSleepTrackingActivity extends AppCompatActivity {

    Button btnYes, btnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(this, ScreenOnAndOffService.class);
        i.setAction("STOPSERVICE");
        stopService(i);

        setContentView(R.layout.activity_stop_sleep_tracking);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getApplicationContext(),ScreenOnAndOffService.class);
                startService(k);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the sleeping time here, and exist.

            }
        });
    }
}
