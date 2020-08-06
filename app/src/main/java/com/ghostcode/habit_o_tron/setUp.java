package com.ghostcode.habit_o_tron;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class setUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        //DataHandler
        final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager();
        final SharedPreferences userData = getSharedPreferences(sharedPrefsManager.preferencesKey, MODE_PRIVATE);

        //Buttons
        Button enter = findViewById(R.id.start);
        Button faq = findViewById(R.id.faq);

        //EditTexts
        final EditText objectiveInput = findViewById(R.id.objectiveInput);
        final EditText dayGoal = findViewById(R.id.dayGoal);

        final TextView charCount = findViewById(R.id.characterCount);
        objectiveInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                charCount.setText("(" + s.length() + "/20)");
                if(s.length() == 20){
                    Toast.makeText(getApplicationContext(),"Maximum character limit reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int cleanDayGoal = -1;
                if(!TextUtils.isEmpty(dayGoal.getText().toString())){
                    cleanDayGoal = Integer.parseInt(dayGoal.getText().toString());
                }
                if(objectiveInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"You have not entered an objective, an objective is required.", Toast.LENGTH_LONG).show();
                }
                else if(cleanDayGoal == 0){
                    Toast.makeText(getApplicationContext(),"You have entered a day goal less than 1. A day goal of more than 0 is required", Toast.LENGTH_LONG).show();
                } else {
                    if ((cleanDayGoal < 0)) {
                        sharedPrefsManager.setUserData(userData, objectiveInput.getText().toString(),0);
                    } else {
                        sharedPrefsManager.setUserData(userData, objectiveInput.getText().toString(), Integer.parseInt(dayGoal.getText().toString()), 0);
                    }
                    Intent intent= new Intent(setUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
