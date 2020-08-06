package com.ghostcode.habit_o_tron;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefsManager {
    //set these to variables in case I want to change the name of these
    public final  String preferencesKey = "userData";
    private final String OBJECTIVE_NAME = "objective";
    private final String GOAL_NAME = "goal";
    private final String DAY = "DAY";
    private final String TAG = "DataHandler";

    public String getUserObjective(SharedPreferences userData){
        Log.v(TAG, "User Objective: " + userData.getString(OBJECTIVE_NAME, null));
        return userData.getString(OBJECTIVE_NAME, null);
    }

    public int getUserObjectiveGoal(SharedPreferences userData){
        Log.v(TAG, "User Goal: " + userData.getInt(GOAL_NAME, 0));
        return userData.getInt(GOAL_NAME, 0);
    }

    public int getUserCurrentDay(SharedPreferences userData){
        Log.v(TAG, "Day: " + userData.getInt(DAY, 0));
        return userData.getInt(DAY, 0);
    }

    public void setUserData(SharedPreferences userData, String objective, int goal, int day){
        SharedPreferences.Editor editor = userData.edit();

        editor.putString(OBJECTIVE_NAME, objective);
        editor.putInt(DAY, day);
        editor.putInt(GOAL_NAME, goal);
        editor.commit();
    }

    public void setUserData(SharedPreferences userData, String objective, int day){
        SharedPreferences.Editor editor = userData.edit();

        editor.putString(OBJECTIVE_NAME, objective);
        editor.putInt(DAY, day);
        editor.commit();
    }

    public void updateDay(SharedPreferences userData, int day){
        SharedPreferences.Editor editor = userData.edit();
        editor.putInt(DAY, day);
        editor.commit();
    }
}
