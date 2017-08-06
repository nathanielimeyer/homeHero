package com.jbnm.homehero.ui.signup;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import io.reactivex.Observable;

/**
 * Created by janek on 8/5/17.
 */

public class SignUpContract {
    interface Presenter extends BasePresenter {
        void init();
        void handleSignUpButtonClick();
        void handleLoginLinkClick();
    }
    interface MvpView extends BaseMvpView {
        void enableSignUpButton();
        void disableSignUpButton();
        Observable<CharSequence> getEmailText();
        Observable<CharSequence> getPasswordText();
        Observable<CharSequence> getConfirmPasswordText();
        void showEmailError();
        void hideEmailError();
        void showPasswordError();
        void hidePasswordError();
        void showConfirmError();
        void hideConfirmError();
        void loginIntent();
    }
}
