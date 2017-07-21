package com.jbnm.homehero.ui.goal;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public interface GoalMvpView {
    void showProgress(String id, String description, int value, String rewardImage, int approvedPoints, int pendingPoints);
    void showLoading();
    void hideLoading();
    void showError();
}
