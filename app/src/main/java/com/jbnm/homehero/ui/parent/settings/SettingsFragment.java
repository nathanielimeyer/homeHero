package com.jbnm.homehero.ui.parent.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseFragment;
import com.jbnm.homehero.ui.login.LoginActivity;
import com.jbnm.homehero.ui.parent.taskList.ParentTaskListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment implements SettingsContract.MvpView {
    private SettingsContract.Presenter presenter;
    private Unbinder unbinder;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(String childId) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CHILD_INTENT_KEY, childId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        String childId = getArguments().getString(Constants.CHILD_INTENT_KEY);

        presenter = new SettingsPresenter(this, childId);
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

    @OnClick(R.id.logoutButton)
    public void logoutButtonClick() {
        presenter.handleLogoutButtonClick();
    }

    @Override
    public void taskEditIntent(String childId) {
        startActivity(ParentTaskListActivity.createIntent(getContext(), childId));
    }

    @Override
    public void rewardEditIntent(String childId) {
//        Intent intent = new Intent(getContext(), RewardEditActivity.class);
//        startActivity(intent);
    }


    @Override
    public void loginIntent() {
        startActivity(LoginActivity.createIntent(getContext()));
        getActivity().finish();
    }
}
