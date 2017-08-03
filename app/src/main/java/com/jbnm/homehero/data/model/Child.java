package com.jbnm.homehero.data.model;

import com.jbnm.homehero.data.remote.FirebasePushIDGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by janek on 7/17/17.
 */

public class Child {
    private String id;
    private int totalPoints;
    private String currentTaskKey;
    private Map<String, Task> tasks = new HashMap<>();
    private List<String> rejectedTasks = new ArrayList<>();
    private String currentRewardKey;
    private Map<String, Reward> rewards = new HashMap<>();
    private List<String> pendingRewards = new ArrayList<>();

    public static Child newInstance() {
        return new Child(FirebasePushIDGenerator.generatePushId());
    }

    public Child() {}

    public Child(String id) {
        this.id = id;
        this.totalPoints = 0;
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

    public List<String> getRejectedTasks() {
        return rejectedTasks;
    }

    public void setRejectedTasks(List<String> rejectedTasks) {
        this.rejectedTasks = rejectedTasks;
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

    public List<String> getPendingRewards() {
        return pendingRewards;
    }

    public void setPendingRewards(List<String> pendingRewards) {
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
        int pendingPoints = 0;
        for(Task task : this.tasks.values()) {
            if (task.pending()) {
                pendingPoints += 1;
            }
        }
        return pendingPoints;
    }

    public void markTaskApproved(String taskId) {
        this.tasks.get(taskId).approveTask();
        this.totalPoints += 1;
    }

    public void markTaskRejected(String taskId) {
        this.tasks.get(taskId).rejectTask();
        if (this.currentTaskKey == null) {
            this.currentTaskKey = taskId;
        } else {
            this.rejectedTasks.add(taskId);
        }
    }

    public void redeemReward() {
        if (this.currentRewardKey != null && this.totalPoints >= this.currentReward().getValue()) {
            this.totalPoints -= this.currentReward().getValue();
            this.pendingRewards.add(this.currentRewardKey);
            this.currentRewardKey = null;
        }
    }

    public boolean allTasksCompleted() {
//      for (Task task : tasks) {
//          if (task.isAvailable()) {
//              return false;
//          }
//      }
      return false;
  }

    public void fulfillReward(String rewardId) {
        this.pendingRewards.remove(rewardId);
        this.rewards.get(rewardId).incrementTimesRedeemed();
    }
}
