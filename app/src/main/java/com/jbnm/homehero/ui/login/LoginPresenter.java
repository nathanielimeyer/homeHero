package com.jbnm.homehero.ui.login;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.data.remote.FirebaseAuthService;

import org.reactivestreams.Subscriber;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by janek on 8/5/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager;
    private FirebaseAuthService authService;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public LoginPresenter(LoginContract.MvpView mvpView) {
        this.mvpView = mvpView;
        dataManager = new DataManager();
        auth = FirebaseAuth.getInstance();
        authService = FirebaseAuthService.getInstance();
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void init() {
        mvpView.disableLoginButton();
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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    processAuthState(user.getUid());
                }
            }
        };
    }

    @Override
    public void handleLoginButtonClick(String email, String password) {
        disposable.add(authService.login(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableMaybeObserver<AuthResult>() {
                    @Override public void onSuccess(AuthResult authResult) {

                    }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void handleSignUpLinkClick() {
        mvpView.signUpIntent();
    }

    @Override
    public void addAuthListener() {
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void removeAuthListener() {
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
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

    private void processAuthState(String userId) {
        disposable.add(dataManager.getChildByParentId(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { determineUserStatus(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    private void determineUserStatus(Child child) {
        Log.d("test", child.getId());
        if (child.getTasks().keySet().size() < Constants.MIN_TASK_COUNT) {
            Log.d("test", "not enough tasks");
            // navigate to modify task list
        } else {
            Log.d("test", "enough tasks");
            // navigate to goal progress
        }
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
