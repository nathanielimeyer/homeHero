package com.jbnm.homehero.ui.taskprogress;

import android.os.Bundle;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;

public class TaskProgressActivity extends BaseActivity implements TaskProgressContract.MvpView {

    private TaskProgressContract.Presenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);

        presenter = new TaskProgressPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
