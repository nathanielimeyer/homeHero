package com.jbnm.homehero.ui.parent.settings;

import com.jbnm.homehero.data.remote.FirebaseAuthService;

/**
 * Created by janek on 8/1/17.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.MvpView mvpView;
    private String childId;

    public SettingsPresenter(SettingsContract.MvpView mvpView, String childId) {
        this.mvpView = mvpView;
        this.childId = childId;
    }

    @Override
    public void detach() {

    }

    @Override
    public void handleTaskEditButtonClick() {
        mvpView.taskEditIntent(childId);
    }

    @Override
    public void handleRewardEditButtonClick() {
        mvpView.rewardEditIntent(childId);
    }

    @Override
    public void handleLogoutButtonClick() {
        FirebaseAuthService.getInstance().logout();
        mvpView.loginIntent();
    }
}
