package com.jbnm.homehero.ui.goal;

import android.content.Context;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalPresenter implements GoalContract.Presenter {
    private GoalContract.MvpView mvpView;
    private Context mContext;
    private Child child;
    private Reward reward;

    public GoalPresenter(GoalContract.MvpView view, Context context) {
        mvpView = view;
        mContext = context;

    }

    @Override
    public void detach() {
        mvpView = null;

    }

    @Override
    public void loadChildData() {
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        Task myTask = new Task("1", "Wash your room", instructions, true );
        List<Task> tasks = Arrays.asList(myTask);
        Reward reward = new Reward("1", "Disneyland", 20, "castle.jpg");
        List<Reward> rewards = Arrays.asList(reward);
        Child myChild = new Child("1", tasks, rewards);

    }

    @Override
    public void checkProgress() {
        if (child.getTotalPoints() >= reward.getValue()) {

        } else {

        }
    }
}

