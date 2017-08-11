package com.jbnm.homehero.ui.reauth;

import com.jbnm.homehero.ui.base.BasePresenter;

/**
 * Created by janek on 8/10/17.
 */

public class ReAuthContract {
    interface Presenter extends BasePresenter {
        void init();
        void handleCancelButtonClick();
        void handleOkButtonClick(String password);
    }
    interface MvpView {
        void dismissDialog();
        void navigateToParent(String childId);
        void showPasswordError();
        void hidePasswordError();
        void showLoading();
        void hideLoading();
    }
}
