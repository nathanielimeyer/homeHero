package com.jbnm.homehero.ui.goal;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalPresenter {
    GoalMvpView mvpView;
    Child child;
    Reward reward;

    public GoalPresenter(GoalMvpView view) {
        mvpView = view;
    }

    public void loadGoalScreenData() {
        // get
    }
}

