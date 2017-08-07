package com.jbnm.homehero.ui.login;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import io.reactivex.Observable;

/**
 * Created by janek on 8/5/17.
 */

public class LoginContract {
    interface Presenter extends BasePresenter {
        void init();
        void handleLoginButtonClick(String email, String password);
        void handleSignUpLinkClick();
        void addAuthListener();
        void removeAuthListener();
    }
    interface MvpView extends BaseMvpView {
        void enableLoginButton();
        void disableLoginButton();
        Observable<CharSequence> getEmailText();
        Observable<CharSequence> getPasswordText();
        void showEmailError();
        void hideEmailError();
        void showPasswordError();
        void hidePasswordError();
        void signUpIntent();
        void goalProgressIntent(String childId);
        void parentTaskListIntent(String childId);
    }
}
