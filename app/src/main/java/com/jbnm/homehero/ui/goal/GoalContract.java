package com.jbnm.homehero.ui.goal;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalContract {
    interface Presenter extends BasePresenter {
        void loadData(String childId);
        void checkProgress();
        void taskButtonClicked();
        void determineTaskButtonStatus();
        void setNewRewardAndDecrementPoints(int i);
        void rewardAnimationEnded();
        List buildRewardDialogList();
        void populateAllTheThings();
    }
    interface MvpView extends BaseMvpView {
        void showProgress(String description, int rewardValue, String rewardImage, int approvedPoints, int pendingPoints);
        void showRewardAnimation();
        void hideTaskButton();
        void showTaskButton();
        void taskPickerIntent(String childId);
        void taskProgressIntent(String childId);
        void setGoalDescription(String description);
        void setGoalImage(String rewardImage);
        void showGoalPickerDialog();
        void buildGoalPickerDialog();
    }
}
