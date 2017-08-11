package com.jbnm.homehero.ui.reauth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by janek on 8/10/17.
 */

public class ReAuthPresenter implements ReAuthContract.Presenter {
    private ReAuthContract.MvpView mvpView;
    private String childId;
    private FirebaseUser user;

    public ReAuthPresenter(ReAuthContract.MvpView mvpView, String childId) {
        this.mvpView = mvpView;
        this.childId = childId;
    }

    @Override
    public void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void detach() {

    }

    @Override
    public void handleCancelButtonClick() {
        mvpView.dismissDialog();
    }

    @Override
    public void handleOkButtonClick(String password) {
        String email = user.getEmail();
        if (email != null && password != null && !password.isEmpty()) {
            mvpView.showLoading();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mvpView.navigateToParent(childId);
                        mvpView.hidePasswordError();
                    } else {
                        mvpView.showPasswordError();
                    }
                    mvpView.hideLoading();
                }
            });
        } else {
            mvpView.showPasswordError();
        }
    }
}
