package com.jbnm.homehero.ui.parent.settings;

/**
 * Created by janek on 8/1/17.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.MvpView mvpView;

    public SettingsPresenter(SettingsContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detach() {

    }

    @Override
    public void handleTaskEditButtonClick() {
        mvpView.taskEditIntent();
    }

    @Override
    public void handleRewardEditButtonClick() {
        mvpView.rewardEditIntent();
    }
}
