package com.jbnm.homehero.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janek on 7/17/17.
 */

public class Child {
  private String id;
  private int totalPoints;
  private Task currentTask;
  private List<Task> tasks = new ArrayList<>();
  private Reward currentReward;
  private List<Reward> rewards = new ArrayList<>();
  private List<Reward> pendingRewards = new ArrayList<>();

  public Child() {}

  public Child(String id, List<Task> tasks, List<Reward> rewards, int totalPoints) {
    this.id = id;
    this.tasks = tasks;
    this.rewards = rewards;
    this.totalPoints = totalPoints;
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

  public Task getCurrentTask() {
    return currentTask;
  }

  public void setCurrentTask(Task currentTask) {
    this.currentTask = currentTask;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }

  public Reward getCurrentReward() {
    return currentReward;
  }

  public void setCurrentReward(Reward currentReward) {
    this.currentReward = currentReward;
  }

  public List<Reward> getRewards() {
    return rewards;
  }

  public void setRewards(List<Reward> rewards) {
    this.rewards = rewards;
  }

  public List<Reward> getPendingRewards() {
    return pendingRewards;
  }

  public void setPendingRewards(List<Reward> pendingRewards) {
    this.pendingRewards = pendingRewards;
  }



  public int calculatePendingPoints() {
//    sum up value of all tasks that return true for pendingApproval
    return 5;
  }

  public List<Task> tasksPendingApproval() {
//    return list of tasks that return true for pendingApproval
    return tasks;
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
}
