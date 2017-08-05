package com.jbnm.homehero.ui.login;

import android.util.Patterns;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by janek on 8/5/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter(LoginContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void init() {
        disposable.add(Observable.combineLatest(
                mvpView.getEmailText(),
                mvpView.getPasswordText(),
                new BiFunction<CharSequence, CharSequence, Boolean>() {
                    @Override public Boolean apply(CharSequence emailText, CharSequence passwordText) throws Exception {
                        boolean validEmail = validateEmail(emailText.toString().trim());
                        boolean validPassword = validatePassword(passwordText.toString().trim());
                        return validEmail && validPassword;
                    }
                }
        ).subscribeWith(new DisposableObserver<Boolean>() {
            @Override public void onNext(Boolean valid) {
                if (valid) {
                    mvpView.enableLoginButton();
                } else {
                    mvpView.disableLoginButton();
                }
            }
            @Override public void onError(Throwable e) { processError(e); }
            @Override public void onComplete() {}
        }));
    }

    @Override
    public void handleLoginButtonClick() {

    }

    private boolean validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mvpView.hideEmailError();
            return true;
        } else {
            mvpView.showEmailError();
            return false;
        }
    }

    private boolean validatePassword(String password) {
        if (password.length() > 5) {
            mvpView.hidePasswordError();
            return true;
        } else {
            mvpView.showPasswordError();
            return false;
        }
    }

    private void processError(Throwable e) {

    }
}
