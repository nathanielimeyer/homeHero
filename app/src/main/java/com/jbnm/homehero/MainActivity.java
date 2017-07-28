package com.jbnm.homehero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
//        setup();
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

    public void setup() {
        Parent parent = new Parent("ParentTestId2", "email@email.com");
        Child child = Child.newInstance();
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        for (int i = 0; i<10; i++) {
            Task task = Task.newInstance("clean your room", instructions);
            child.addTask(task);
        }
        for (int i =0; i<2; i++) {
            Reward reward = Reward.newInstance("Disneyland", 20, "castle.jpg");
            child.addReward(reward);
        }

        DataManager dataManager = new DataManager();
        dataManager.saveParent(parent).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Parent>() {
            @Override public void onSubscribe(Disposable d) {}
            @Override public void onNext(Parent parent) {}
            @Override public void onError(Throwable e) {}
            @Override public void onComplete() {
                Log.d("test", "parent saved");
            }
        });

        dataManager.saveChild(child).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Child>() {
            @Override public void onSubscribe(Disposable d) {}
            @Override public void onNext(Child child) {}
            @Override public void onError(Throwable e) {}
            @Override public void onComplete() {
                Log.d("test", "child saved");
            }
        });
    }
}