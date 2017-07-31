package com.jbnm.homehero.ui.taskprogress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskProgressActivity extends BaseActivity implements TaskProgressContract.MvpView {
    @BindView(R.id.taskCompleteButton) Button taskCompleteButton;
    @BindView(R.id.taskDescriptionTextView) TextView taskDescriptionTextView;
    @BindView(R.id.taskInstructionsListView) ListView taskInstructionsListView;

    private TaskProgressContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);
        ButterKnife.bind(this);

        presenter = new TaskProgressPresenter(this);

        taskCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleTaskCompleteClick();
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
            taskDescriptionTextView.setText(task.getDescription());
            ArrayAdapter<String> instructionsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    task.getInstructions());
            taskInstructionsListView.setAdapter(instructionsAdapter);
        }
    }

    @Override
    public void goalProgressIntent() {
        Intent intent = new Intent(TaskProgressActivity.this, GoalActivity.class);
        startActivity(intent);
    }
}
