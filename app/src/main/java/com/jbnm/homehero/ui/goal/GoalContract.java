package com.jbnm.homehero.ui.goal;

import android.content.Context;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalContract {
    interface Presenter extends BasePresenter {
        void loadData();
        void checkProgress();
        void taskButtonClicked();
        void determineTaskButtonStatus();
    }
    interface MvpView extends BaseMvpView {
        void showProgress(String description, int rewardValue, String rewardImage, int approvedPoints, int pendingPoints);
        void showRewardAnimation();
//        void showGoalPickerDialog();
        void hideTaskButton();
        void showTaskButton();
        void taskPickerIntent();
        void taskProgressIntent();
        void setGoalDescription(String description);
        void setGoalImage(String rewardImage);
    }
}
