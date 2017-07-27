package com.jbnm.homehero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;
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
        Intent intent = new Intent(MainActivity.this, GoalActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonTask)
    public void taskButton() {
        Intent intent = new Intent(MainActivity.this, TaskPickerActivity.class);
        startActivity(intent);
    }
}