package com.jbnm.homehero.ui.taskprogress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.taskpicker.TaskPickerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskProgressActivity extends BaseActivity implements TaskProgressContract.MvpView {
    @BindView(R.id.taskCompleteButton) Button taskCompleteButton;
    @BindView(R.id.goalProgressButton) Button goalProgressButton;
    @BindView(R.id.taskDescriptionTextView) TextView taskDescriptionTextView;
    @BindView(R.id.taskInstructionsRecyclerView) RecyclerView taskInstructionsRecyclerView;
    @BindView(R.id.taskIconImageView) ImageView taskIconImageView;

    private TaskProgressContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra(getString(R.string.childId_intent_key));

        presenter = new TaskProgressPresenter(this);
        presenter.loadTask(childId);

        taskCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleTaskCompleteClick();
            }
        });

        goalProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleGoalProgressClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void showTask(Task task) {
        if (task != null) {
            taskIconImageView.setImageResource(getTaskIcon(task));
            taskDescriptionTextView.setText(task.getDescription());
            setUpAdapter(task.getInstructions());
        }
    }

    private int getTaskIcon(Task task) {
        if (task.getIcon() != null) {
            return getResources().getIdentifier(task.getIcon(), "drawable", getPackageName());
        } else {
            // TODO: add default task icon here
            return getResources().getIdentifier("down_arrow", "drawable", getPackageName());
        }
    }

    private void setUpAdapter(List<String> instructions) {
        taskInstructionsRecyclerView.setAdapter(new TaskInstructionsAdapter(instructions));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskInstructionsRecyclerView.setLayoutManager(layoutManager);
        taskInstructionsRecyclerView.setHasFixedSize(true);
        taskInstructionsRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public void goalProgressIntent() {
        Intent intent = new Intent(TaskProgressActivity.this, TaskPickerActivity.class);
//        Intent intent = new Intent(TaskProgressActivity.this, GoalActivity.class);
        startActivity(intent);
    }
}
