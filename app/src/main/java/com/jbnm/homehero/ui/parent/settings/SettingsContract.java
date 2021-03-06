package com.jbnm.homehero.ui.parent.settings;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

/**
 * Created by janek on 8/1/17.
 */

public class SettingsContract {
    interface Presenter extends BasePresenter {
        void handleTaskEditButtonClick();
        void handleRewardEditButtonClick();
        void handleLogoutButtonClick();
    }
    interface MvpView extends BaseMvpView {
        void taskEditIntent(String childId);
        void rewardEditIntent(String childId);
        void loginIntent();
    }
}
