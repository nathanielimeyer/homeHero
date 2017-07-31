package com.jbnm.homehero.ui.taskprogress;

import android.content.Intent;
import android.os.Bundle;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;

public class TaskProgressActivity extends BaseActivity implements TaskProgressContract.MvpView {

    private TaskProgressContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);

        presenter = new TaskProgressPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void showTask(Task task) {

    }

    @Override
    public void goalProgressIntent() {
        Intent intent = new Intent(TaskProgressActivity.this, GoalActivity.class);
        startActivity(intent);
    }
}
