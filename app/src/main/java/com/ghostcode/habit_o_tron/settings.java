package com.ghostcode.habit_o_tron;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView versionName = findViewById(R.id.version);
        versionName.setText(BuildConfig.VERSION_NAME);
    }
}
