package com.jbnm.homehero.data.model;

import com.jbnm.homehero.data.remote.FirebasePushIDGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by janek on 7/17/17.
 */

public class Child {
    private String id;
    private int totalPoints;
    private String currentTask;
    private Map<String, Boolean> tasks = new HashMap<>();
    private String currentReward;
    private Map<String, Boolean> rewards = new HashMap<>();
    private Map<String, Boolean> pendingRewards = new HashMap<>();

    public static Child newInstance() {
        return new Child(FirebasePushIDGenerator.generatePushId());
    }

    public Child() {}

//  public Child(String id, Map<String, Boolean> tasks, Map<String, Boolean> rewards) {
//    this.id = id;
//    this.tasks = tasks;
//    this.rewards = rewards;
//    this.totalPoints = 0;
//  }

    public Child(String id) {
        this.id = id;
        this.totalPoints = 10;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(String currentTask) {
        this.currentTask = currentTask;
    }

    public Map<String, Boolean> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Boolean> tasks) {
        this.tasks = tasks;
    }

    public String getCurrentReward() {
        return currentReward;
    }

    public void setCurrentReward(String currentReward) {
        this.currentReward = currentReward;
    }

    public Map<String, Boolean> getRewards() {
        return rewards;
    }

    public void setRewards(Map<String, Boolean> rewards) {
        this.rewards = rewards;
    }

    public Map<String, Boolean> getPendingRewards() {
        return pendingRewards;
    }

    public void setPendingRewards(Map<String, Boolean> pendingRewards) {
        this.pendingRewards = pendingRewards;
    }



    public int calculatePendingPoints() {
//    sum up value of all tasks that return true for pendingApproval
        return 5;
    }

    public void markTaskApproved(Task task) {
//    mark task as approved
//    add points to total points
//    check if total points is enough to redeem prize
    }

    public void redeemReward() {
//    subtract currentReward value from totalPoints
//    trigger notification
//    add currentReward to pendingRewards
    }

    public void fulfillReward(Reward reward) {
//    remove reward from pendingRewards
//    increment timesRedeemed from reward in list of all rewards
  }

  public boolean allTasksCompleted() {
//      for (Task task : tasks) {
//          if (task.isAvailable()) {
//              return false;
//          }
//      }
      return true;
  }
}
