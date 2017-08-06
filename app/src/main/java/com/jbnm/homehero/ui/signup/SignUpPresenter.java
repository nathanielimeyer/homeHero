package com.jbnm.homehero.ui.signup;

/**
 * Created by janek on 8/5/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mvpView;

    public SignUpPresenter(SignUpContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detach() {

    }
}
