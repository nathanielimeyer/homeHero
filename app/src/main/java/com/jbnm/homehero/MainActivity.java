package com.jbnm.homehero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.data.remote.FirebaseService;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.ui.goal.GoalActivity;
import com.jbnm.homehero.ui.taskpicker.TaskPickerActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        testFirebase();
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

    private void testFirebase() {
        String childId = "-Kpqt2--Ma5rMfjj3z4z";
        DataManager dataManager = new DataManager();

        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        Task task = new Task("1", "Wash your room", instructions, true);
        Task newTask = Task.newInstance("clean your room", instructions);

        Reward reward = new Reward("1", "Disneyland", 20, "castle.jpg");
        Reward newReward = Reward.newInstance("Disneyland", 20, "castle.jpg");

        dataManager.saveReward(childId, newReward).subscribeWith(new DisposableObserver<Reward>() {
            @Override public void onNext(Reward responseBody) {
                Log.d("test", responseBody.getId());
            }

            @Override public void onError(Throwable e) {
                Log.d("test", "error: " + e.toString());
            }

            @Override public void onComplete() {
                Log.d("test", "complete");
            }
        });

    }
}