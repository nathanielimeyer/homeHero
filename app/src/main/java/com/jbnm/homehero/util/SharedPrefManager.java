package com.jbnm.homehero.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by janek on 8/7/17.
 */

public class SharedPrefManager {
    private static final String PREFS_NAME = "USER_DATA";

    public static final String TASKS_CREATED = "tasks_created";
    public static final String GOALS_CREATED = "goals_created";

    private SharedPreferences sharedPreferences;
    private static SharedPrefManager instance;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean getTasksCreated() {
        return sharedPreferences.getBoolean(TASKS_CREATED, false);
    }

    public void setTasksCreated(boolean status) {
        sharedPreferences.edit().putBoolean(TASKS_CREATED, status).apply();
    }

    public boolean getGoalsCreated() {
        return sharedPreferences.getBoolean(GOALS_CREATED, false);
    }

    public void setGoalsCreated(boolean status) {
        sharedPreferences.edit().putBoolean(GOALS_CREATED, status).apply();
    }
}
