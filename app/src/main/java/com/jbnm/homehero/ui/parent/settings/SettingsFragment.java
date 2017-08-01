package com.jbnm.homehero.ui.parent.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseFragment;

import butterknife.ButterKnife;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SettingsPresenter(this);
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
        return R.layout.fragment_settings;
    }


}
