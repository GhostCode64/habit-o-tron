package com.ghostcode.habit_o_tron;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class debugMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_menu);

       //buttons list
        Button main = findViewById(R.id.debug_main);
        Button setup = findViewById(R.id.debug_setUp);
        Button advice = findViewById(R.id.debug_advice);
        Button stats = findViewById(R.id.debug_stats);

        TextView lastPulse = findViewById(R.id.lastPulse);

        DBManager dbManager = new DBManager(this);

        ArrayList pulses = dbManager.getAllPulses();

        lastPulse.setText("Last pulse: " + pulses.get(pulses.size() - 1));

        //imageViews
        ImageView back = findViewById(R.id.debugBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(debugMenu.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(debugMenu.this, setUp.class);
                startActivity(intent);
                finish();
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(debugMenu.this, advice.class);
                startActivity(intent);
                finish();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(debugMenu.this, userStats.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
