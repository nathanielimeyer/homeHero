package com.jbnm.homehero.data.model;

import com.jbnm.homehero.data.remote.FirebasePushIDGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by janek on 7/17/17.
 */

public class Task {
    private String id;
    private String description;
    private String icon;
    private List<String> instructions = new ArrayList<>();
    private boolean available;
    private long lastCompleted;

    public static Task newInstance(String description, String icon, List<String> instructions) {
        return new Task(FirebasePushIDGenerator.generatePushId(), description, icon, instructions, true);
    }

    public Task() {}

    public Task(String id, String description, String icon, List<String> instructions, boolean available) {
        this.id = id;
        this.description = description;
        this.instructions = instructions;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getLastCompleted() {
        return lastCompleted;
    }

    public void setLastCompleted(long lastCompleted) {
        this.lastCompleted = lastCompleted;
    }



    public void markTaskComplete() {
        setAvailable(false);
    }

    public void markTaskApproved() {
        setAvailable(true);
        setLastCompleted(truncateDate(new Date()));
    }

    private boolean notCompletedToday() {
        return lastCompleted < truncateDate(new Date());
    }

    public boolean availableForSelection() {
        return available && notCompletedToday();
    }

    public boolean pendingApproval() {
        return !available && notCompletedToday();
    }

    private static long truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
