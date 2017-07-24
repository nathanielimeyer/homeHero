package com.jbnm.homehero.ui.goal;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalContract {
    interface Presenter extends BasePresenter {
        void loadChildData();
        void checkProgress();
    }
    interface MvpView extends BaseMvpView {
        void showProgress(String description, int rewardValue, String rewardImage, int approvedPoints, int pendingPoints);
        void showRewardAnimation();
    }
}
