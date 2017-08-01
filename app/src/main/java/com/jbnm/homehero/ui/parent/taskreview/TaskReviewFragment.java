package com.jbnm.homehero.ui.parent.taskreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskReviewFragment extends BaseFragment implements TaskReviewContract.MvpView {
    private TaskReviewContract.Presenter presenter;
    private Unbinder unbinder;

    public TaskReviewFragment() {}

    public static TaskReviewFragment newInstance() {
        TaskReviewFragment fragment = new TaskReviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TaskReviewPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_task_review;
    }

}
