package com.jbnm.homehero;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;
import com.jbnm.homehero.ui.taskpicker.TaskPickerActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
        startActivity(TaskPickerActivity.createIntent(this, Constants.CHILDID));
    }
}