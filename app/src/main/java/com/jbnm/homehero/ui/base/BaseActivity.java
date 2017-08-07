package com.jbnm.homehero.ui.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.MainActivity;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.ui.login.LoginActivity;
import com.jbnm.homehero.ui.parent.ParentActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public class BaseActivity extends AppCompatActivity implements BaseMvpView {

    private ProgressBar progressBar;
    private FrameLayout contentFrame;
    private long animationDuration;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        presenter = new BasePresenterImpl(this);
        presenter.setupAuth();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.addAuthListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeAuthListener();
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout rootView = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        progressBar = rootView.findViewById(R.id.progressBar);
        contentFrame = rootView.findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        super.setContentView(rootView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this instanceof ParentActivity) {
            getMenuInflater().inflate(R.menu.parent, menu);
        } else {
            getMenuInflater().inflate(R.menu.child, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_parent_item) {
            startActivity(ParentActivity.createIntent(this, Constants.CHILDID));
            return true;
        } else if(item.getItemId() == R.id.menu_child_item) {
            startActivity(new Intent(BaseActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean showLoading() {
        contentFrame.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void hideLoading() {
        contentFrame.setAlpha(0f);
        contentFrame.setVisibility(View.VISIBLE);

        contentFrame.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        progressBar.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean showError(String errorMessage) {
        Snackbar errorSnackbar = Snackbar.make(contentFrame, errorMessage, Snackbar.LENGTH_LONG);
        errorSnackbar.show();
        return false;
    }

    @Override
    public void hideError() {

    }

    private void proccessAuthState(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(LoginActivity.createIntent(this));
        } else {
//            DataManager dataManager = new DataManager();
//            dataManager.getParent(firebaseAuth.getCurrentUser().getUid())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new DisposableObserver<Parent>() {
//                        @Override
//                        public void onNext(Parent parent) {
//                            if (parent == null) {
//                                Log.d("test", "no parent");
//                            } else {
//                                Log.d("test", "parent");
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            showError(e.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Log.d("test", "complete");
//                        }
//                    });
        }
    }
}
