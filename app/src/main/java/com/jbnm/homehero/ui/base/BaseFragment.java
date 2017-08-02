package com.jbnm.homehero.ui.base;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jbnm.homehero.R;

public abstract class BaseFragment extends Fragment implements BaseMvpView {

    private ProgressBar progressBar;
    private FrameLayout contentFrame;
    private long animationDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        progressBar = rootView.findViewById(R.id.fragmentProgressBar);
        contentFrame = rootView.findViewById(R.id.fragmentContentFrame);
        inflater.inflate(getFragmentLayout(), contentFrame, true);
        return rootView;
    }

    protected abstract int getFragmentLayout();

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
}
