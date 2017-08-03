package com.jbnm.homehero.data.model;

import com.jbnm.homehero.data.remote.FirebasePushIDGenerator;
import com.jbnm.homehero.util.DateUtil;

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
    private long lastCompleted;
    private TaskState state;

    public static Task newInstance(String description, String icon, List<String> instructions) {
        return new Task(FirebasePushIDGenerator.generatePushId(), description, icon, instructions);
    }

    public Task() {}

    public Task(String id, String description, String icon, List<String> instructions) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.instructions = instructions;
        this.state = TaskState.INCOMPLETE;
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

    public long getLastCompleted() {
        return lastCompleted;
    }

    public void setLastCompleted(long lastCompleted) {
        this.lastCompleted = lastCompleted;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }



    public void completeTask() {
        setState(TaskState.PENDING);
    }

    public void rejectTask() {
        setState(TaskState.REJECTED);
    }

    public void approveTask() {
        setState(TaskState.INCOMPLETE);
        setLastCompleted(DateUtil.truncateDate(new Date()));
    }

    public boolean availableForSelection() {
        return state == TaskState.INCOMPLETE && notCompletedToday();
    }

    public boolean pending() {
        return state == TaskState.PENDING;
    }

    public TaskStatus taskStatus() {
        if (state == TaskState.PENDING) {
            return TaskStatus.PENDING;
        } else if (state == TaskState.REJECTED) {
            return TaskStatus.INCOMPLETE;
        } else if (notCompletedToday()) {
            return TaskStatus.INCOMPLETE;
        } else {
            return TaskStatus.COMPLETE;
        }
    }

    private boolean notCompletedToday() {
        return lastCompleted < DateUtil.truncateDate(new Date());
    }
}
