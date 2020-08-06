package com.ghostcode.habit_o_tron;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Background colour array
    private ArrayList<ImageView> colours = new ArrayList<ImageView>();

    //Button List
    private Button failure;
    private Button completed;
    private Button advice;
    private Button stats;
    private Button debug;

    //Progress Bar
    private ProgressBar completionRate;

    //TextViews
    private TextView status;
    private TextView detailedProgress;
    private TextView currentDayText;
    private TextView objective;
    private TextView quote;
    private TextView objectiveIndicator;
    private TextView alreadyDone;
    private TextView versionName;

    //images
    //ImageView icon;
    ImageView navdotone;
    ImageView navdottwo;

    //Misc Variables
    String lastPulse;
    String[] lineAndQuoteInfo = new String[1];
    int currentDay;
    boolean appWasMinimised = false;

    //Managers
    final SharedPrefsManager sharedPrefsManager = new SharedPrefsManager();
    final DBManager dbManager = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Background colour array
        colours.add(findViewById(R.id.amberGrad));
        colours.add(findViewById(R.id.redGrad));

        //Button List
        failure = findViewById(R.id.faq);
        completed = findViewById(R.id.taskComplete);
        advice = findViewById(R.id.advice);
        stats = findViewById(R.id.stats);
        //debug = findViewById(R.id.debugButton);

        //Progress Bar
        completionRate = findViewById(R.id.progressBar);

        //TextViews
        status = findViewById(R.id.status);
        detailedProgress = findViewById(R.id.progressDetailed);
        currentDayText = findViewById(R.id.currentDay);
        objective = findViewById(R.id.objective);
        quote = findViewById(R.id.inspirationalQuote);
        objectiveIndicator = findViewById(R.id.currentObjectiveIndicator);
        alreadyDone = findViewById(R.id.alreadyDone);
        versionName = findViewById(R.id.version);

        //images
        //icon = findViewById(R.id.icon);
        navdotone = findViewById(R.id.navdot1);
        navdottwo = findViewById(R.id.navdot2);

        //TODO build database
        //TODO store objective
        //TODO store current progress
        //TODO import/export function
        //TODO other pages
        //TODO get inspirational quotes

        //Startup stuff

        final AssetManager assetManager = getResources().getAssets(); //AssetManager must be in here

        //Get user data
        SharedPreferences userData = getSharedPreferences(sharedPrefsManager.preferencesKey, MODE_PRIVATE);
        String objectiveData = sharedPrefsManager.getUserObjective(userData);
        if(objectiveData == null){
            Intent intent= new Intent(MainActivity.this, setUp.class);
            startActivity(intent);
            finish();
        }

        //misc variables
        Random rand = new Random();
        final int daysGoal =  sharedPrefsManager.getUserObjectiveGoal(userData);;
        currentDay = sharedPrefsManager.getUserCurrentDay(userData);

        ArrayList pulses = dbManager.getAllPulses();

        //Set the objective
        objective.setText(objectiveData);

        //Generate quote
        //TODO: put quotes into a user editable file

        InputStream input = null;
        String lineIWant = "";

        try {
            int value = rand.nextInt(countLines(assetManager));
            input = assetManager.open("quotes.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            for (int j = 1; j < value; ++j)
                br.readLine();
            lineIWant = br.readLine();
            lineAndQuoteInfo = lineIWant.split("[|]");
        } catch (IOException e) {
            System.out.println("Oops, something went wrong");
        }

        quote.setText(lineAndQuoteInfo[0]);

        lastPulse = getLastPulse(dbManager);

        //versionName.setText(BuildConfig.VERSION_NAME);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        //set everything invisible
        objective.setVisibility(View.INVISIBLE);
        status.setVisibility(View.INVISIBLE);
        detailedProgress.setVisibility(View.INVISIBLE);
        quote.setVisibility(View.INVISIBLE);
        currentDayText.setVisibility(View.INVISIBLE);
        objectiveIndicator.setVisibility(View.INVISIBLE);

        failure.setVisibility(View.INVISIBLE);
        completed.setVisibility(View.INVISIBLE);
        advice.setVisibility(View.INVISIBLE);
        stats.setVisibility(View.INVISIBLE);

        //icon.setVisibility(View.INVISIBLE);
        completionRate.setVisibility(View.INVISIBLE);

        //fade everything in
        fadeIn(objective);
        fadeIn(detailedProgress);
        fadeIn(quote);
        fadeIn(objectiveIndicator);

        fadeIn(advice);
        fadeIn(stats);
        //fadeIn(debug);

        //fadeIn(icon);
        fadeIn(navdotone);
        fadeIn(navdottwo);

        if(lastPulse.equals(dateFormatter.format(date))){
            completed.setEnabled(false);
            failure.setEnabled(false);

            status.setVisibility(View.GONE);

            fadeIn(alreadyDone);
        } else {
            fadeIn(status);

            fadeIn(completed);
            fadeIn(failure);
        }

        if(daysGoal > 0){
            fadeIn(completionRate);
            //Set up the progress bar
            updateProgress(currentDay, daysGoal);
        } else{
            if(currentDay == 1){
                detailedProgress.setText(currentDay + " day completed");
            } else {
                detailedProgress.setText(currentDay + " days completed");
            }
            detailedProgress.setTextSize(30);
            completionRate.setVisibility(View.GONE);
        }

        //Button actions
        completed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //increment the current day number
                currentDay = currentDay + 1;

                //update the progress bar
                updateProgress(currentDay, daysGoal);
                sharedPrefsManager.updateDay(userData, currentDay);

                //fade the warning text and fade to blue
                fadeOut(status);
                fadeToBlue(colours);

                completed.setEnabled(false);
                failure.setEnabled(false);
                completed.setText("Good job!");

                dbManager.open();
                dbManager.insertNewCompletionTime();
                dbManager.close();
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fadeToAmber(colours);
            }
        });

        failure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fadeToRed(colours);
            }
        });

        /*debug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, debugMenu.class);
                startActivity(intent);
            }
        });*/

        advice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, advice.class);
                startActivity(intent);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, userStats.class);
                startActivity(intent);
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (quote.getText().equals(lineAndQuoteInfo[0])){
                    quote.setText("Quote information:\n" + lineAndQuoteInfo[1]);
                    navdotone.setAlpha((float) 0.25);
                    navdottwo.setAlpha((float) 1);
                } else {
                    quote.setText(lineAndQuoteInfo[0]);
                    navdotone.setAlpha((float) 1);
                    navdottwo.setAlpha((float) 0.25);
                }
            }
        });

        checkTimeStatus(colours, "APP BOOT PROCESS", status);
    }

    @Override
    public void onResume(){

        super.onResume();

        //TODO: Check if the app has been updated today so it isn't accidentally updated with a warning

        //check if it's past 7pm, and display a warning if it is
        checkTimeStatus(colours, "APP RESUME PROCESS", status);

        //quote.setText("The app appears to have been minimised, please close and reopen to ensure there are no issues!");
    }

    private String getLastPulse(DBManager dbManager){
        ArrayList pulses = dbManager.getAllPulses();

        try {
            lastPulse = (String) pulses.get(pulses.size() - 1);
            lastPulse = lastPulse.split(" ")[0];
        } catch (Exception ex) {
            lastPulse = "01/01/1970";
            Log.e("APP BOOT", "No entries in the database");
        }

        return lastPulse;
    }

    private void checkTimeStatus(ArrayList<ImageView> colours, String caller, TextView status){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        lastPulse = getLastPulse(dbManager);

        if(!lastPulse.equals(dateFormatter.format(date))) {
            try {
                //get the current time
                Date currentTime = Calendar.getInstance().getTime();
                Log.v(caller, "Current Time: " + currentTime);

                //set the alert time
                Date alertTime = new SimpleDateFormat("HH:mm:ss").parse("19:00:00");
                Log.d(caller, "alertTime = " + alertTime.getHours() + " | currentTime = " + currentTime.getHours());

                //if it's past 7pm (19:00:00), display the warning
                if (alertTime.getHours() <= currentTime.getHours()) {
                    fadeToAmber(colours);
                    status.setText("It's past 7pm and you haven't set the status of your objective today!");
                } else { //if it isn't (app was left on overnight for example) reset the display
                    status.setText("You haven't set the status of your activity today");
                    fadeToBlue(colours);
                }
            } catch (Exception Ex) {
                //some error idk
                Log.e(caller, "Failure to check the time, error: \n" + Ex);
            }
        }
    }

    //TODO perhaps abstract this
    public static int countLines(AssetManager assetManager) throws IOException {
        try (InputStream inputFile = assetManager.open("quotes.txt")) {
            byte[] c = new byte[1024];

            int readChars = inputFile.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024; ) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = inputFile.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = inputFile.read(c);
            }

            return count == 0 ? 1 : count;
        }
    }

    //Colour fades
    private void fadeToBlue(ArrayList<ImageView> colours){
        fadeOut(colours.get(0));    //0 is amber
        fadeOut(colours.get(1));    //1 is red
    }

    private void fadeToAmber(ArrayList<ImageView> colours){
        fadeIn(colours.get(0));
        fadeOut(colours.get(1));
    }

    private void fadeToRed(ArrayList<ImageView> colours){
        fadeOut(colours.get(0));
        fadeIn(colours.get(1));
    }

    private void fadeOut(ImageView img)
    {
        //if the image isn't already hidden
        if(img.getVisibility() != View.INVISIBLE) {
            Animation fadeOut = new AlphaAnimation(1, 0); //fade from 100% opacity to 100% transparent
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(1000); //duration in milliseconds

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    img.setVisibility(View.INVISIBLE); //once the animation is done, make the image disappear
                }

                public void onAnimationRepeat(Animation animation) {}

                public void onAnimationStart(Animation animation) {}
            });

            img.startAnimation(fadeOut);
        }
    }

    private void fadeOut(TextView text)
    {
        //if the text isn't already hidden
        if(text.getVisibility() != View.INVISIBLE) {
            Animation fadeOut = new AlphaAnimation(1, 0); //fade from 100% opacity to 100% transparent
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(1000); //duration in milliseconds

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    text.setVisibility(View.INVISIBLE); //once the animation is done, make the text invisible
                }

                public void onAnimationRepeat(Animation animation) {}

                public void onAnimationStart(Animation animation) {}
            });

            text.startAnimation(fadeOut);
        }
    }

    private void fadeIn(ImageView img)
    {
        if(img.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            img.startAnimation(fadeIn);
            img.setVisibility(View.VISIBLE);
        }
    }

    private void fadeIn(TextView text)
    {
        if(text.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            text.startAnimation(fadeIn);
            text.setVisibility(View.VISIBLE);
        }
    }

    private void fadeIn(ProgressBar bar)
    {
        if(bar.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            bar.startAnimation(fadeIn);
            bar.setVisibility(View.VISIBLE);
        }
    }

    private void fadeIn(Button button)
    {
        if(button.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            button.startAnimation(fadeIn);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void updateProgress(int currentDay, int daysGoal){
        if(daysGoal > 0) {
            if (completionRate.getProgress() < 100) {
                float division = (float) currentDay / daysGoal; //cast to float and divide, dividing ints results in an int. Who could have guess? Stumped me for about an hour rofl
                completionRate.setProgress(Math.round(division * 100)); //set the progress, but round the result of the division (progress works out of 100 not 1 thus expects an int)
                detailedProgress.setText("You are " + completionRate.getProgress() + "% of the way to your goal! (Day " + currentDay + " of " + daysGoal + " completed)"); //set the detailed progress
            }
            if (completionRate.getProgress() == 100) {
                detailedProgress.setText("Congrats! You've reached your goal! See the stats page to reset or get a new goal!"); //if we are equal to 100, set a congratulations message...
                completionRate.setVisibility(View.GONE); //...and hide the progress bar
            }
        } else {
            if (currentDay == 1){
                detailedProgress.setText(currentDay + " day completed");
            } else {
                detailedProgress.setText(currentDay + " days completed");
            }
        }
    }
}
