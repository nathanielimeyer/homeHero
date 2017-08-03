package com.jbnm.homehero.ui.parent.taskreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskReviewFragment extends BaseFragment implements TaskReviewContract.MvpView, TaskItemClickListener {
    @BindView(R.id.taskReviewRecyclerView) RecyclerView taskReviewRecyclerView;

    private TaskReviewContract.Presenter presenter;
    private Unbinder unbinder;

    public TaskReviewFragment() {}

    public static TaskReviewFragment newInstance(String childId) {
        TaskReviewFragment fragment = new TaskReviewFragment();
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

        presenter = new TaskReviewPresenter(this);
        presenter.loadTasks(childId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
        unbinder.unbind();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_task_review;
    }

    @Override
    public void showTasks(List<Object> tasks) {
        TaskReviewAdapter taskReviewAdapter = new TaskReviewAdapter(tasks);
        taskReviewAdapter.setTaskItemClickListener(this);
        taskReviewRecyclerView.setAdapter(taskReviewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        taskReviewRecyclerView.setLayoutManager(layoutManager);
        taskReviewRecyclerView.setHasFixedSize(true);
        taskReviewRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
    }

    @Override
    public void onTaskItemApprove(String taskId) {
        presenter.handleTaskApprove(taskId);
    }

    @Override
    public void onTaskItemReject(String taskId) {
        presenter.handleTaskReject(taskId);
    }
}
