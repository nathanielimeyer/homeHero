package com.jbnm.homehero.data.model;

/**
 * Created by janek on 8/2/17.
 */

// Status for displaying tasks in parent screen
public enum TaskStatus {
    PENDING("Pending"),
    INCOMPLETE("Incomplete"),
    COMPLETE("Complete");

    private String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
