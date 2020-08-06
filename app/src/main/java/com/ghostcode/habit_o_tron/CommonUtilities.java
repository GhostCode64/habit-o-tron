package com.ghostcode.habit_o_tron;

import android.content.Context;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommonUtilities {

    DBManager dbManager;
    SharedPrefsManager sharedPreferences;

    public CommonUtilities(DBManager this_dbManager, SharedPrefsManager this_sharedPreferences){
        dbManager = this_dbManager;
        sharedPreferences =  this_sharedPreferences;
    }

    public String getLastPulse(DBManager dbManager){
        ArrayList pulses = dbManager.getAllPulses();
        String lastPulse;

        try {
            lastPulse = (String) pulses.get(pulses.size() - 1);
            lastPulse = lastPulse.split(" ")[0];
        } catch (Exception ex) {
            lastPulse = "01/01/1970";
            Log.e("APP BOOT", "No entries in the database");
        }

        return lastPulse;
    }

    public static int countLines(AssetManager assetManager) throws IOException {
        InputStream inputFile = assetManager.open("quotes.txt");
        try {
            byte[] c = new byte[1024];

            int readChars = inputFile.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = inputFile.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = inputFile.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            inputFile.close();
        }
    }

    public void checkTimeStatus(ArrayList<ImageView> colours, String caller, TextView status, Context context){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        String lastPulse = getLastPulse(dbManager);

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
                    fadeToAmber(colours, context);
                    status.setText("It's past 7pm and you haven't set the status of your objective today!");
                } else { //if it isn't (app was left on overnight for example) reset the display
                    status.setText("You haven't set the status of your activity today");
                    fadeToBlue(colours, context);
                }
            } catch (Exception Ex) {
                //some error idk
                Log.e(caller, "Failure to check the time, error: \n" + Ex);
            }
        }
    }

    //Colour fades
    public void fadeToBlue(ArrayList<ImageView> colours, Context context){
        fadeOut(colours.get(0));    //0 is amber
        fadeOut(colours.get(1));    //1 is red
    }

    public void fadeToAmber(ArrayList<ImageView> colours, Context context){
        fadeIn(colours.get(0), context);
        fadeOut(colours.get(1));
    }

    public void fadeToRed(ArrayList<ImageView> colours, Context context){
        fadeOut(colours.get(0));
        fadeIn(colours.get(1), context);
    }

    public void fadeOut(ImageView img)
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

    public void fadeOut(TextView text)
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

    public void fadeIn(ImageView img, Context context)
    {
        if(img.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            img.startAnimation(fadeIn);
            img.setVisibility(View.VISIBLE);
        }
    }

    public void fadeIn(TextView text, Context context)
    {
        if(text.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            text.startAnimation(fadeIn);
            text.setVisibility(View.VISIBLE);
        }
    }

    public void fadeIn(ProgressBar bar, Context context)
    {
        if(bar.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            bar.startAnimation(fadeIn);
            bar.setVisibility(View.VISIBLE);
        }
    }

    public void fadeIn(Button button, Context context)
    {
        if(button.getVisibility() != View.VISIBLE) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            button.startAnimation(fadeIn);
            button.setVisibility(View.VISIBLE);
        }
    }
}
