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
    private String currentTaskKey;
    private Map<String, Task> tasks = new HashMap<>();
    private String currentRewardKey;
    private Map<String, Reward> rewards = new HashMap<>();
    private Map<String, Reward> pendingRewards = new HashMap<>();

    public static Child newInstance() {
        return new Child(FirebasePushIDGenerator.generatePushId());
    }

    public Child() {}

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

    public String getCurrentTaskKey() {
        return currentTaskKey;
    }

    public void setCurrentTaskKey(String currentTaskKey) {
        this.currentTaskKey = currentTaskKey;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public String getCurrentRewardKey() {
        return currentRewardKey;
    }

    public void setCurrentRewardKey(String currentRewardKey) {
        this.currentRewardKey = currentRewardKey;
    }

    public Map<String, Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Map<String, Reward> rewards) {
        this.rewards = rewards;
    }

    public Map<String, Reward> getPendingRewards() {
        return pendingRewards;
    }

    public void setPendingRewards(Map<String, Reward> pendingRewards) {
        this.pendingRewards = pendingRewards;
    }


    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public Task currentTask() {
        return this.tasks.get(this.currentTaskKey);
    }

    public void addReward(Reward reward) {
        this.rewards.put(reward.getId(), reward);
    }

    public Reward currentReward() {
        return this.rewards.get(this.currentRewardKey);
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
//    subtract currentRewardKey value from totalPoints
//    trigger notification
//    add currentRewardKey to pendingRewards
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
      return false;
  }
}
