package com.jbnm.homehero.ui.login;

import android.util.Log;
import android.util.Patterns;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.remote.FirebaseAuthService;

import org.reactivestreams.Subscriber;

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
    private FirebaseAuthService authService;

    public LoginPresenter(LoginContract.MvpView mvpView) {
        this.mvpView = mvpView;
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
        disposable.add(authService.getAuthState()
                .filter(new Predicate<FirebaseAuth>() {
                    @Override public boolean test(FirebaseAuth firebaseAuth) throws Exception {
                        return firebaseAuth.getCurrentUser() != null;
                    }
                })
                .map(new Function<FirebaseAuth, String>() {
                    @Override public String apply(FirebaseAuth firebaseAuth) throws Exception {
                        return firebaseAuth.getCurrentUser().getUid();
                    }
                })
                .distinctUntilChanged()
                .flatMap(new Function<String, ObservableSource<Child>>() {
                    @Override public ObservableSource<Child> apply(String userId) throws Exception {
                        return authService.getChild(userId);
                    }
                })
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { determineUserStatus(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
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
//        disposable.add(authService.loginAndGetChild(email, password)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new ResourceSubscriber<Child>() {
//                    @Override public void onNext(Child child) { determineUserStatus(child);}
//                    @Override public void onError(Throwable t) { processError(t); }
//                    @Override public void onComplete() {}
//                }));
//        disposable.add(authService.login(email, password).subscribeWith(new DisposableMaybeObserver<AuthResult>() {
//            @Override public void onSuccess(AuthResult authResult) {
//
//            }
//            @Override public void onError(Throwable e) { processError(e); }
//            @Override public void onComplete() {}
//        }));
    }

    @Override
    public void handleSignUpLinkClick() {
        mvpView.signUpIntent();
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

    private void determineUserStatus(Child child) {
        Log.d("test", child.getId());
        if (child.getTasks().keySet().size() < Constants.MIN_TASK_COUNT) {
            // navigate to modify task list
        } else {
            // navigate to goal progress
        }
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
