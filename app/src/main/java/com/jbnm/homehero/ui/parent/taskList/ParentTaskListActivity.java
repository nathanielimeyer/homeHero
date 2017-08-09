package com.jbnm.homehero.ui.parent.taskList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.taskEdit.TaskEditorActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListActivity extends BaseActivity implements ParentTaskListContract.MvpView, ParentTaskClickListener {
    @BindView(R.id.taskListRecyclerView) RecyclerView taskListRecyclerView;
    @BindView(R.id.addTaskButton) Button addTaskButton;
    @BindView(R.id.buttonBackToSettings) Button backButton;

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

        childId = getIntent().getStringExtra("childId");

        presenter = new ParentTaskListPresenter(this);
        presenter.loadTasks(childId);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addTaskButtonClicked();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void listTasks(List<Task> tasks) {
        this.tasks = tasks;
        ParentTaskListAdapter parentTaskListAdapter = new ParentTaskListAdapter(tasks, context);
        parentTaskListAdapter.setParentTaskClickListener(this);
        taskListRecyclerView.setAdapter(parentTaskListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);
        taskListRecyclerView.setHasFixedSize(true);
        taskListRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public void addTaskIntent(String childId) {
        startActivity(TaskEditorActivity.createIntent(this, childId, "newTask"));
    }

    @Override
    public void onEditTask(String taskId) {

        startActivity(TaskEditorActivity.createIntent(this, childId, taskId));
    }

    @Override
    public void onDeleteTask(Task task) {
        tasks.remove(task);
        presenter.saveTasks(tasks);
    }
}
