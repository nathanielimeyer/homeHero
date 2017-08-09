package com.jbnm.homehero.ui.signup;

import android.util.Patterns;

import com.jbnm.homehero.data.remote.FirebaseAuthService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by janek on 8/5/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private FirebaseAuthService authService;

    public SignUpPresenter(SignUpContract.MvpView mvpView) {
        this.mvpView = mvpView;
        authService = FirebaseAuthService.getInstance();
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void init() {
        mvpView.disableSignUpButton();
        disposable.add(Observable.combineLatest(
                mvpView.getEmailText(),
                mvpView.getPasswordText(),
                mvpView.getConfirmPasswordText(),
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override public Boolean apply(CharSequence emailText, CharSequence passwordText, CharSequence confirmText) throws Exception {
                        boolean validEmail = validateEmail(emailText.toString().trim());
                        boolean validPassword = validatePassword(passwordText.toString().trim());
                        boolean validPasswordMatch = validatePasswordMatch(passwordText.toString().trim(), confirmText.toString().trim());
                        return validEmail && validPassword && validPasswordMatch;
                    }
                }
        ).subscribeWith(new DisposableObserver<Boolean>() {
            @Override public void onNext(Boolean valid) {
                if (valid) {
                    mvpView.enableSignUpButton();
                } else {
                    mvpView.disableSignUpButton();
                }
            }
            @Override public void onError(Throwable e) { processError(e); }
            @Override public void onComplete() {}
        }));
    }

    @Override
    public void handleSignUpButtonClick(String email, String password) {
        mvpView.showLoading();
        disposable.add(authService.createUser(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override public void run() throws Exception {
                        mvpView.loginIntent();
                        mvpView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        processError(throwable);
                    }
                }));
    }

    @Override
    public void handleLoginLinkClick() {
        mvpView.loginIntent();
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

    private boolean validatePasswordMatch(String password, String confirm) {
        if (password.equals(confirm)) {
            mvpView.hideConfirmError();
            return true;
        } else {
            mvpView.showConfirmError();
            return false;
        }
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
