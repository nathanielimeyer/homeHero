package com.jbnm.homehero.ui.login;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth auth;

    public LoginPresenter(LoginContract.MvpView mvpView) {
        this.mvpView = mvpView;
        auth = FirebaseAuth.getInstance();
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
    public void handleLoginButtonClick(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    
                } else {
                    mvpView.showError("Login failed. Please try again.");
                }
            }
        });
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
