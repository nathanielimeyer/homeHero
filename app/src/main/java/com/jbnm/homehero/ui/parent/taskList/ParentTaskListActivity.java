package com.jbnm.homehero.ui.parent.taskList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.ParentActivity;
import com.jbnm.homehero.ui.taskEdit.TaskEditorActivity;
import com.jbnm.homehero.util.SharedPrefManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListActivity extends BaseActivity implements ParentTaskListContract.MvpView, ParentTaskClickListener {
    @BindView(R.id.taskListRecyclerView) RecyclerView taskListRecyclerView;
    @BindView(R.id.addTaskButton) FloatingActionButton addTaskButton;

    List<Task> tasks;
    String childId;
    private Context context = this;
    private ParentTaskListContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId) {
        Intent intent = new Intent(context, ParentTaskListActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_task_list);
        ButterKnife.bind(this);

        childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);

        presenter = new ParentTaskListPresenter(this, SharedPrefManager.getInstance(this));
        presenter.loadTasks(childId);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addTaskButtonClicked();
            }
        });

        taskListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               if (newState == RecyclerView.SCROLL_INDICATOR_TOP && addTaskButton.isEnabled()) {
                    addTaskButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addTaskButton.isShown()) {
                    addTaskButton.hide();
                } else if (dy < 0 && !addTaskButton.isShown() && addTaskButton.isEnabled()) {
                    addTaskButton.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        presenter.handleBackButtonPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void listTasks(List<Task> tasks) {
        this.tasks = tasks;
        ParentTaskListAdapter parentTaskListAdapter = new ParentTaskListAdapter(tasks);
        parentTaskListAdapter.setParentTaskClickListener(this);
        taskListRecyclerView.setAdapter(parentTaskListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);
        taskListRecyclerView.setHasFixedSize(true);
        taskListRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public void addTaskIntent(String childId) {
        startActivity(TaskEditorActivity.createIntent(this, childId, Constants.TASK_NEW_INTENT_VALUE));
    }

    @Override
    public void onEditTask(String taskId) {
        startActivity(TaskEditorActivity.createIntent(this, childId, taskId));
    }

    @Override
    public void parentIntent(String childId) {
        startActivity(ParentActivity.createIntent(this, childId));
        finish();
    }

    @Override
    public void onDeleteTask(Task task) {
        presenter.checkAndNullCurrentTask(task.getId());
        tasks.remove(task);
        presenter.saveTasks(tasks);
    }

    @Override
    public void setAddTaskButtonEnabled(boolean enabled) {
        addTaskButton.setEnabled(enabled);
        if (enabled) {
            addTaskButton.setVisibility(View.VISIBLE);
        } else {
            addTaskButton.setVisibility(View.GONE);
        }
    }
}
