package com.jbnm.homehero.ui.parent;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.util.SharedPrefManager;

/**
 * Created by janek on 8/7/17.
 */

public class ParentPresenter implements ParentContract.Presenter {
    private ParentContract.MvpView mvpView;
    private SharedPrefManager sharedPrefManager;
    private String childId;

    public ParentPresenter(ParentContract.MvpView mvpView, SharedPrefManager sharedPrefManager) {
        this.mvpView = mvpView;
        this.sharedPrefManager = sharedPrefManager;
    }

    @Override
    public void init(String childId) {
        this.childId = childId;
        mvpView.setToolbarTitle(Constants.PARENT_TITLE);
        if (!sharedPrefManager.getTasksCreated()) {
            mvpView.taskListIntent(childId);
        } else if (!sharedPrefManager.getGoalsCreated()) {
            mvpView.rewardListIntent(childId);
        } else {
            mvpView.setUpViewPager(childId);
        }
    }

    @Override
    public void detach() {
        sharedPrefManager = null;
    }


    @Override
    public void handleChildNavButtonClick() {
        mvpView.goalIntent(childId);
    }

}
