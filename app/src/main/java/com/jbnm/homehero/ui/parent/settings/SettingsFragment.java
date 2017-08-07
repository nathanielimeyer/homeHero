package com.jbnm.homehero.ui.parent.settings;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseFragment;
import com.jbnm.homehero.ui.parent.taskList.ParentTaskListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment implements SettingsContract.MvpView {
    private SettingsContract.Presenter presenter;
    private Unbinder unbinder;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter = new SettingsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detach();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @OnClick(R.id.manageTaskButton)
    public void taskEditButtonClick() {
        presenter.handleTaskEditButtonClick();
    }

    @OnClick(R.id.manageRewardButton)
    public void rewardEditButtonClick() {
        presenter.handleRewardEditButtonClick();
    }

    @Override
    public void taskEditIntent() {
        Intent intent = new Intent(getContext(), ParentTaskListActivity.class);
        startActivity(intent);
    }

    @Override
    public void rewardEditIntent() {
//        Intent intent = new Intent(getContext(), RewardEditActivity.class);
//        startActivity(intent);
    }
}
