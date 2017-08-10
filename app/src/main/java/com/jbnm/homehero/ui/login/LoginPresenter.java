package com.jbnm.homehero.ui.login;

import android.util.Patterns;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.data.remote.FirebaseAuthService;
import com.jbnm.homehero.util.SharedPrefManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by janek on 8/5/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager;
    private SharedPrefManager sharedPrefManager;
    private FirebaseAuthService authService;

    public LoginPresenter(LoginContract.MvpView mvpView, SharedPrefManager sharedPrefManager) {
        this.mvpView = mvpView;
        this.sharedPrefManager = sharedPrefManager;
        dataManager = new DataManager();
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

        mvpView.showLoading();
        disposable.add(authService.getAuthState()
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged(new Function<FirebaseAuth, Object>() {
                    @Override public Object apply(FirebaseAuth firebaseAuth) throws Exception {
                        return firebaseAuth.getCurrentUser();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<FirebaseAuth>() {
                    @Override public void accept(FirebaseAuth firebaseAuth) throws Exception {
                        if (firebaseAuth.getCurrentUser() == null) {
                            mvpView.hideLoading();
                        }
                    }
                })
                .flatMap(new Function<FirebaseAuth, ObservableSource<Child>>() {
                    @Override public ObservableSource<Child> apply(FirebaseAuth firebaseAuth) throws Exception {
                        if (firebaseAuth.getCurrentUser() != null) {
                            return dataManager.getChildByParentId(firebaseAuth.getCurrentUser().getUid());
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { determineUserStatus(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void handleLoginButtonClick(String email, String password) {
        mvpView.showLoading();
        disposable.add(authService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableMaybeObserver<AuthResult>() {
                    @Override public void onSuccess(AuthResult authResult) {}
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
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
        mvpView.hideLoading();
        sharedPrefManager.setTasksCreated(child.getTasks().keySet().size() >= Constants.MIN_TASK_COUNT);
        sharedPrefManager.setGoalsCreated(child.getRewards().keySet().size() >= Constants.MIN_REWARD_COUNT);
        if (sharedPrefManager.getTasksCreated() && sharedPrefManager.getGoalsCreated()) {
            mvpView.goalProgressIntent(child.getId());
        } else {
            mvpView.parentIntent(child.getId());
        }
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
