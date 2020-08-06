package com.ghostcode.habit_o_tron;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.*;

import java.util.ArrayList;
import java.util.List;

public class userStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        final DBManager dbManager = new DBManager(this);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();

        entries = dbManager.getAllPulsesForGraph(entries);

        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
        dataSet.setColor(Color.WHITE);
        dataSet.setValueTextColor(Color.WHITE);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getDescription().setEnabled(false);
        //chart.setDescription("fdfsd");// styling, ...
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        chart.invalidate(); // refresh

        ImageView back = findViewById(R.id.statsBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(userStats.this, settings.class);
                startActivity(intent);
            }
        });
    }
}
