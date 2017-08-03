package com.jbnm.homehero.ui.taskEdit;

import android.os.Bundle;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;

public class TaskEditorActivity extends BaseActivity implements TaskEditorContract.MvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);
    }
}
