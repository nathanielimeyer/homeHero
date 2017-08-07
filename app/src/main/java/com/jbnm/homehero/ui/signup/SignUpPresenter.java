package com.jbnm.homehero.ui.signup;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.remote.FirebaseAuthService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function3;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by janek on 8/5/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
//    private FirebaseAuth auth;
    private FirebaseAuthService authService;

    public SignUpPresenter(SignUpContract.MvpView mvpView) {
        this.mvpView = mvpView;
//        auth = FirebaseAuth.getInstance();
        authService = FirebaseAuthService.getInstance();
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
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    processNewUser(task.getResult().getUser());
//                } else {
//                    mvpView.showError("Registration failed. Please try again.");
//                }
//            }
//        });
        disposable.add(authService.createUserAndGetChild(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { determineUserStatus(child); }
                    @Override public void onError(Throwable t) { processError(t); }
                    @Override public void onComplete() {}
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

//    private void processNewUser(FirebaseUser user) {
//        Parent newParent = new Parent(user.getUid(), user.getEmail());
//        Child newChild = Child.newInstance();
//
//    }

    private void determineUserStatus(Child child) {
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
