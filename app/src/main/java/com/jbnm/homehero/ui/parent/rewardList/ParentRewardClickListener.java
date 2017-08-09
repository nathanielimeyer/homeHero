package com.jbnm.homehero.ui.parent.rewardList;

import com.jbnm.homehero.data.model.Reward;

/**
 * Created by nathanielmeyer on 8/8/17.
 */

public interface ParentRewardClickListener {
    void onEditReward(String rewardId);
    void onDeleteReward(Reward reward);
}
