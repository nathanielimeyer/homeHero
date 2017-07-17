package com.jbnm.homehero.data.model;

/**
 * Created by janek on 7/17/17.
 */

public class Reward {
  private String id;
  private String description;
  private int value;
  private int timesRedeemed;
  private String rewardImage;

  public Reward() {}

  public Reward(String id, String description, int value, String rewardImage) {
    this.id = id;
    this.description = description;
    this.value = value;
    this.rewardImage = rewardImage;
    this.timesRedeemed = 0;
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

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getTimesRedeemed() {
    return timesRedeemed;
  }

  public void setTimesRedeemed(int timesRedeemed) {
    this.timesRedeemed = timesRedeemed;
  }

  public String getRewardImage() {
    return rewardImage;
  }

  public void setRewardImage(String rewardImage) {
    this.rewardImage = rewardImage;
  }
}
