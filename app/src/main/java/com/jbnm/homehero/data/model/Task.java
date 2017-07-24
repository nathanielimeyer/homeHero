package com.jbnm.homehero.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janek on 7/17/17.
 */

public class Task {
  private String id;
  private String description;
  private List<String> instructions = new ArrayList<>();
  private boolean available;
  private long lastCompleted;

  public Task() {}

  public Task(String id, String description, List<String> instructions, boolean available) {
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
//    set available to false;
  }

  public void markTaskApproved() {
//    set available to true && update lastCompleted;
  }

  public boolean hasBeenCompletedToday() {
//    return true if available and lastCompleted is not today.
    return true;
  }

  public boolean pendingApproval() {
//    return true is available is false and lastCompleted is not today.
    return true;
  }
}
