package com.jbnm.homehero;

import android.content.Intent;
import android.os.Bundle;

import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;
import com.jbnm.homehero.ui.taskEdit.TaskEditorActivity;
import com.jbnm.homehero.ui.taskpicker.TaskPickerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonGoal)
    public void goalButton() {
        startActivity(GoalActivity.createIntent(this, Constants.CHILDID));
    }

    @OnClick(R.id.buttonEditTask)
    public void editTaskButton() {
        Intent intent = new Intent(MainActivity.this, TaskEditorActivity.class);
        intent.putExtra("childId", "-Kq5YlmM3saCunGh6Jr_");
        startActivity(intent);
    }


    @OnClick(R.id.buttonTask)
    public void taskButton() {
        startActivity(TaskPickerActivity.createIntent(this, Constants.CHILDID));
    }
}